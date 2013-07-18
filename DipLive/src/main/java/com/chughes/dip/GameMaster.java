package com.chughes.dip;

import java.util.Arrays;
import java.util.Vector;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chughes.dip.GameEntity.Stage;

import dip.world.Power;

@Component
public class GameMaster {
	
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

}
