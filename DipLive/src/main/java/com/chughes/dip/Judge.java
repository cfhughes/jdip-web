package com.chughes.dip;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.GameEntity.Stage;
import com.chughes.security.UserEntity;
import com.chughes.service.GameService;

import dip.gui.order.GUIOrderFactory;
import dip.process.Adjustment;
import dip.process.Adjustment.AdjustmentInfoMap;
import dip.process.StdAdjudicator;
import dip.world.TurnState;

@Service
public class Judge {
	
	private static final int ACCEPTABLE_MISSES = 3;
	
	private @Autowired GameService gs;
	protected @Autowired SessionFactory sessionFactory;
	protected @Autowired Mailer mailer;

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
		}
		sessionFactory.getCurrentSession().update(ge.getW());
		updateInfo(ge);
		sessionFactory.getCurrentSession().saveOrUpdate(ge);
	}

	public void updateInfo(GameEntity game) {
		game.setPhase(game.getW().getLastTurnState().getPhase().toString());
		for (UserGameEntity player : game.getPlayers()) {
			if (!player.isReady()){
				player.setMissed(player.getMissed()+1);
				if (player.getMissed() > ACCEPTABLE_MISSES){
					gs.removeUserFromGame(game, player.getUser());
				}
			}
			int supply = game.getW().getLastTurnState().getPosition().getOwnedSupplyCenters(game.getW().getMap().getPower(player.getPower())).length;
			System.out.println(game.getW().getMap().getPower(player.getPower()));
			player.setSupply_centers(supply);
			if (player.getUser().getId() != UserEntity.NULL_USER.getId()){
				player.setReady(false);
				mailer.newphase(player.getUser().getEmail(), game.getName());
			}
			if (game.getTurnlength() != 0){
				long milis = new Date().getTime() + (60L * 60L * 1000L * game.getTurnlength());
				Date end = new Date(milis);
				game.setTurnend(end);
			}

		}



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
