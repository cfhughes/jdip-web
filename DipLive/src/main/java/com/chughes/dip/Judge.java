package com.chughes.dip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.data.GameRepository;

import dip.gui.order.GUIOrderFactory;
import dip.process.StdAdjudicator;

@Service
public class Judge {
	
	@Autowired private GameRepository gameR;

	@Transactional
	public void advanceGame(int game){
		GameEntity ge = gameR.findById(game);
		StdAdjudicator stdJudge = new StdAdjudicator(new GUIOrderFactory(), ge.getW().getLastTurnState());
		stdJudge.process();
		ge.getW().setTurnState(stdJudge.getNextTurnState());
		gameR.updateWorld(ge.getW());
		for (UserGameEntity player : ge.getPlayers()) {
			player.setReady(false);
		}
		ge.setPhase(ge.getW().getLastTurnState().getPhase().toString());
		
		gameR.saveGame(ge);
	}
	
}
