package com.chughes.dip;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.GraphApi;
import org.springframework.social.facebook.api.OpenGraphOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.chughes.dip.GameEntity.Stage;
import com.chughes.security.UserEntity;
import com.chughes.service.GameService;

import dip.gui.order.GUIOrderFactory;
import dip.process.Adjustment;
import dip.process.Adjustment.AdjustmentInfoMap;
import dip.process.StdAdjudicator;
import dip.world.Phase.PhaseType;
import dip.world.Power;
import dip.world.TurnState;

@Service
public class Judge {
	
	private static final int ACCEPTABLE_MISSES = 3;
	
	private @Autowired GameService gs;
	protected @Autowired SessionFactory sessionFactory;
	protected @Autowired Mailer mailer;
	private @Autowired Facebook facebookApp;
	private @Autowired UsersConnectionRepository ucr;

	@Transactional
	public void advanceGame(GameEntity ge){
		
		//Adjudicate orders
		StdAdjudicator stdJudge = new StdAdjudicator(new GUIOrderFactory(), ge.getW().getLastTurnState());
		stdJudge.process();
		TurnState ts = stdJudge.getNextTurnState();
		if (ts != null){
			ge.getW().setTurnState(ts);
		}
		//End Game if Victory Occurs
		if (ge.getW().getLastTurnState().isEnded()){
			endGame(ge);
		}else{
			updateInfo(ge);
		}
		sessionFactory.getCurrentSession().update(ge.getW());

	}
	
	public void endGame(GameEntity ge){
		ge.setStage(Stage.ENDED);
		ge.setPhase("Ended");
		AdjustmentInfoMap info = Adjustment.getAdjustmentInfo(ge.getW().getLastTurnState(), ge.getW().getRuleOptions(), ge.getW().getMap().getPowers());
		int total = ge.getW().getLastTurnState().getPosition().getOwnedSupplyCenters().length;
		for (UserGameEntity player : ge.getPlayers()) {
			int owned = info.get(ge.getW().getMap().getPower(player.getPower())).getSupplyCenterCount();
			player.setVictory_share(((float)owned)/((float)total));
			if (owned > 0) {
				player.getUser().setWins(player.getUser().getWins()+1);
			}else {
				player.getUser().setLosses(player.getUser().getLosses()+1);
			}
			sessionFactory.getCurrentSession().update(player.getUser());
		}
		sessionFactory.getCurrentSession().saveOrUpdate(ge);
	}

	public void updateInfo(GameEntity game) {
		game.setPhase(game.getW().getLastTurnState().getPhase().toString());
		for (UserGameEntity player : game.getPlayers()) {
			player.setOrderable(true);
			if (!player.isReady()){
				player.setMissed(player.getMissed()+1);
				if (player.getMissed() > ACCEPTABLE_MISSES){
					gs.removeUserFromGame(game, player.getUser());
				}
			}
			Power power = game.getW().getMap().getPower(player.getPower());
			int supply = game.getW().getLastTurnState().getPosition().getOwnedSupplyCenters(power).length;

			//System.out.println(game.getW().getMap().getPower(player.getPower()));
			player.setSupply_centers(supply);
			
			if (!player.getUser().getUsername().equals("EMPTY")){
				player.setReady(false);
				//Email Notify
				mailer.newphase(player.getUser().getEmail(), game.getName());
				//Facebook Notify
				ConnectionRepository cr = ucr.createConnectionRepository(player.getUser().getId()+"");
				List<Connection<Facebook>> fb = cr.findConnections(Facebook.class);
				if (fb.size() == 1){
					MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
					map.set("href", "/game/"+game.getId());
					map.set("template", "Your game titled '"+game.getName()+"' has advanced to the next phase.");
					String uri = GraphApi.GRAPH_API_URL + fb.get(0).getKey().getProviderUserId() + "/notifications";
					Map<String, Object> result = facebookApp.restOperations().postForObject(uri, map, Map.class);
					if (!result.containsKey("success")){
						System.out.println("Facebook returned: "+result.get("message"));
					}
				}
				//Determine players that have no Orders to give
				if (game.getW().getLastTurnState().getPosition().getUnitProvinces(power).length == 0 && game.getW().getLastTurnState().getPhase().getPhaseType() == PhaseType.MOVEMENT){
					player.setOrderable(false);
					player.setReady(true);
				}else if (game.getW().getLastTurnState().getPhase().getPhaseType() == PhaseType.ADJUSTMENT && supply == game.getW().getLastTurnState().getPosition().getUnitProvinces(power).length){
					player.setOrderable(false);
					player.setReady(true);
				}else if (game.getW().getLastTurnState().getPhase().getPhaseType() == PhaseType.RETREAT && game.getW().getLastTurnState().getPosition().getDislodgedUnitProvinces(power).length == 0){
					player.setOrderable(false);
					player.setReady(true);
				}
			}
			//Determine next phase end
			if (game.getTurnlength() != 0){
				long milis = new Date().getTime() + (60L * 60L * 1000L * game.getTurnlength());
				Date end = new Date(milis);
				game.setTurnend(end);
			}
			
		}
		sessionFactory.getCurrentSession().saveOrUpdate(game);

	}

	@Transactional
	public void cron() {
		Query q = sessionFactory.getCurrentSession().createQuery("from GameEntity where turnend < current_timestamp() and stage= 'PLAYING'");
		List<GameEntity> list = q.list();
		for (GameEntity ge : list){
			advanceGame(ge);
		}
		
	}

}
