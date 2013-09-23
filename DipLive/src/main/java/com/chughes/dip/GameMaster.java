package com.chughes.dip;

import java.util.Arrays;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chughes.dip.GameEntity.Stage;
import com.chughes.service.GameService;

import dip.gui.order.GUIOrderFactory;
import dip.order.OrderFactory;
import dip.process.StdAdjudicator;
import dip.world.Power;

@Service
public class GameMaster {

	@Autowired private GameService gameS;

	private static final Logger logger = LoggerFactory.getLogger(GameMaster.class);

	//Every minute
	@Scheduled(cron="0 * * * * ?")
	public void resolveGames(){
		System.out.println("Executed");
	}

	@Async
	public void beginGame(GameEntity game){
		game.setStage(Stage.PLAYING);
		Vector<Power> powers = new Vector<Power>(Arrays.asList(game.getW().getMap().getPowers()));
		//Simple way to choose powers
		for (UserGameEntity player : game.getPlayers()) {
			int chosen = (int) Math.floor(Math.random()*powers.size());
			player.setPower(powers.remove(chosen).getName());
		}
	}

	@Async
	public void processGame(GameEntity ge){
		try{
			for (UserGameEntity player : ge.getPlayers()) {
				if (!player.isReady()){

					return;
				}
			}

			StdAdjudicator stdJudge = new StdAdjudicator(new GUIOrderFactory(), ge.getW().getLastTurnState());
			stdJudge.process();
			ge.getW().setTurnState(stdJudge.getNextTurnState());
			gameS.saveGame(ge);
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

}
