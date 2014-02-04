package com.chughes.dip;

import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.chughes.data.GameRepository;
import com.chughes.dip.GameEntity.Stage;

import dip.world.Power;

@Service
public class GameMaster {

	@Autowired private Judge j;
	@Autowired private GameRepository gr;

	private static final Logger logger = LoggerFactory.getLogger(GameMaster.class);
	private static volatile boolean busy = false;

	//Every minute
	@Scheduled(cron="0 * * * * ?")
	public void resolveGames(){
		if (!busy){
			busy = true;
			j.cron();
			busy = false;
		}
	}

	public void beginGame(GameEntity game){
		game.setStage(Stage.PLAYING);

		Vector<Power> powers = new Vector<Power>(Arrays.asList(game.getW().getMap().getPowers()));
		//Simple way to choose powers
		for (UserGameEntity player : game.getPlayers()) {
			int chosen = (int) Math.floor(Math.random()*powers.size());
			player.setPower(powers.remove(chosen).getName());
		}
		j.updateInfo(game);
	}


	public void processGame(GameEntity ge){
		if (busy){
			return;
		}
		try{
			for (UserGameEntity player : ge.getPlayers()) {
				if (!player.isReady()){
					return;
				}
			}
			for (UserGameEntity player : ge.getPlayers()) {
				player.setOrderable(false);
			}

			ge.setTurnend(new Date());
			gr.updateGame(ge);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

}
