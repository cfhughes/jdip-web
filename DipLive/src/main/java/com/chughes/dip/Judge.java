package com.chughes.dip;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.data.GameRepository;
import com.chughes.dip.GameEntity.Stage;

import dip.gui.order.GUIOrderFactory;
import dip.process.Adjustment;
import dip.process.Adjustment.AdjustmentInfoMap;
import dip.process.StdAdjudicator;
import dip.world.Position;
import dip.world.Power;
import dip.world.TurnState;

@Service
public class Judge {
	
	protected @Autowired SessionFactory sessionFactory;

	@Transactional
	public void advanceGame(int game){
		GameEntity ge = (GameEntity) sessionFactory.getCurrentSession().get(GameEntity.class, game);
		//Adjudicate orders
		StdAdjudicator stdJudge = new StdAdjudicator(new GUIOrderFactory(), ge.getW().getLastTurnState());
		stdJudge.process();
		TurnState ts = stdJudge.getNextTurnState();
		if (ts != null){
			ge.getW().setTurnState(ts);
		}
		ge.setPhase(ge.getW().getLastTurnState().getPhase().toString());
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
		for (UserGameEntity player : ge.getPlayers()) {
			player.setReady(false);
		}
		sessionFactory.getCurrentSession().saveOrUpdate(ge);
	}

}
