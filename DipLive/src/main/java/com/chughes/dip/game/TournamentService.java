package com.chughes.dip.game;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chughes.dip.game.GameEntity.Stage;
import com.chughes.dip.user.UserEntity;

@Service
public class TournamentService {
	
	@Autowired GameService gs;
	
	HashMap<Integer,Integer> num = new HashMap<Integer,Integer>();
 	HashMap<Integer,Integer> gemap = new HashMap<Integer,Integer>();
	
	public int joinTournament(UserEntity ue) throws Exception{
		if (!num.containsKey(ue.getLevel())){
			num.put(ue.getLevel(), 1);
		}
		if (!gemap.containsKey(ue.getLevel())){
			GameEntity ge = new GameEntity();
			ge.setName("Tournament Game - Level "+ue.getLevel()+" - "+num.get(ue.getLevel()));
			num.put(ue.getLevel(), num.get(ue.getLevel())+1);
			ge.setTurnlength(48);
			ge.setTournament(true);
			ge.setSecret("");
			
			gs.newGame("Standard", ge);
			
			gemap.put(ue.getLevel(), ge.getId());
		}
		GameEntity ge = gs.getGame(gemap.get(ue.getLevel()));
		gs.addUserToGame(ge, ue, "");
		
		int id = gemap.get(ue.getLevel());
		
		if (ge.getStage() == Stage.PLAYING){
			gemap.remove(ue.getLevel());
		}
		
		return id;
		
	}

}
