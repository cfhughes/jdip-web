package com.chughes.dip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.data.GameRepository;
import com.chughes.dip.GameEntity.Stage;

import dip.gui.order.GUIOrderFactory;
import dip.process.StdAdjudicator;
import dip.world.TurnState;

@Service
public class Judge {
	
	@Autowired private GameRepository gameR;

	@Transactional
	public void advanceGame(int game){
		GameEntity ge = gameR.findById(game);
		StdAdjudicator stdJudge = new StdAdjudicator(new GUIOrderFactory(), ge.getW().getLastTurnState());
		stdJudge.process();
		TurnState ts = stdJudge.getNextTurnState();
		if (ts != null){
			ge.getW().setTurnState(ts);
		}
		ge.setPhase(ge.getW().getLastTurnState().getPhase().toString());
		if (ge.getW().getLastTurnState().isEnded()){
			ge.setStage(Stage.ENDED);
			ge.setPhase("Ended");
		}
		gameR.updateWorld(ge.getW());
		for (UserGameEntity player : ge.getPlayers()) {
			player.setReady(false);
		}
		gameR.saveGame(ge);
	}
	
}
