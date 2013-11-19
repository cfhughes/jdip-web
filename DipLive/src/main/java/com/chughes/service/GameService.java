package com.chughes.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.data.GameRepository;
import com.chughes.dip.GameEntity;
import com.chughes.dip.GameEntity.Stage;
import com.chughes.dip.GameMaster;
import com.chughes.dip.HomeController;
import com.chughes.dip.UserGameEntity;
import com.chughes.security.UserDAO;
import com.chughes.security.UserEntity;

@Service
public class GameService {

	@Autowired private GameRepository gameRepo;
	@Autowired private UserDAO userRepo;
	@Autowired private GameMaster gm;
	
	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	public void addUserToGame(GameEntity game, UserEntity user, String secret){
		if (game.getStage() != Stage.PREGAME){
			return;
		}
		if (gameRepo.inGameUser(game.getId(), user.getId()) != null){
			return;
		}
		if (game.getSecret() != null && game.getSecret().length() > 0 && !game.getSecret().equals(secret)) {
			return;
		}
		UserGameEntity uge = new UserGameEntity();
		uge.setGame(game);
		uge.setUser(user);		
		Set<UserGameEntity> players = game.getPlayers();
		players.add(uge);
		if (players.size() == game.getMaxplayers()){
			gm.beginGame(game);
		}
		game.setPlayers(players);
		user.addGame(uge);

		gameRepo.saveInGameUser(uge);
		
		gameRepo.updateGame(game);
		userRepo.updateUser(user);
		
	}
	
	public void removeUserFromGame(GameEntity game, UserEntity user){
		if (user.getId() == UserEntity.NULL_USER.getId())return;//Can't remove null user
		UserGameEntity uge = gameRepo.inGameUser(game.getId(), user.getId());
		uge.setUser(UserEntity.NULL_USER);
		uge.setMissed(0);
		uge.setReady(true);
		user.getGames().remove(uge);
		//Number of times user has left a game
		user.setRetreats(user.getRetreats()+1);
		
		userRepo.updateUser(UserEntity.NULL_USER);
		userRepo.updateUser(user);
		gameRepo.saveInGameUser(uge);
	}

	public void saveGame(GameEntity ge){
		gameRepo.saveGame(ge);
	}

	public List<GameEntity> searchGames(){
		return gameRepo.queryGames();
	}

	public GameEntity getGame(int id){
		return gameRepo.findById(id);
	}
	

}
