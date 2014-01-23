package com.chughes.service;

import java.util.List;
import java.util.Set;

import javax.activity.InvalidActivityException;

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
		game.setPlayers(players);
		user.addGame(uge);

		gameRepo.saveInGameUser(uge);
		
		gameRepo.updateGame(game);
		userRepo.updateUser(user);
		
	}
	
	public void removeUserFromGame(GameEntity game, UserEntity user){
		if (user.getUsername().equals("EMPTY"))return;//Can't remove null user
		if (game.getStage() == Stage.ENDED){
			throw new RuntimeException("Can't Remove User From Ended Game");
		}
		UserGameEntity uge = gameRepo.inGameUser(game.getId(), user.getId());
		user.getGames().remove(uge);
		if (game.getStage() == Stage.PREGAME){
			userRepo.updateUser(user);
			game.getPlayers().remove(uge);
			gameRepo.updateGame(game);
			gameRepo.deleteInGameUser(uge);
			return;
		}
		uge.setUser(UserEntity.NULL_USER);
		uge.setMissed(0);
		uge.setReady(true);

		//Number of times user has left a game
		user.setRetreats(user.getRetreats()+1);
		
		userRepo.updateUser(UserEntity.NULL_USER);
		userRepo.updateUser(user);
		gameRepo.saveInGameUser(uge);
	}
	
	public void replaceUserInGame(GameEntity ge, Integer r, UserEntity newuser){
		UserGameEntity uge = gameRepo.inGameUser(r);
		UserEntity replace = uge.getUser();
		if (gameRepo.inGameUser(ge.getId(), newuser.getId()) != null)return;
		if (!replace.getUsername().equals("EMPTY"))return; //For now can only replace empty users
		if (ge.getSecret() != null && ge.getSecret().length() > 0)return; 
		if (!ge.getPlayers().contains(uge))return;
		uge.setUser(newuser);
		uge.setMissed(0);
		if (uge.isOrderable())uge.setReady(false);
		replace.getGames().remove(uge);
		newuser.getGames().add(uge);
		
		userRepo.updateUser(replace);
		userRepo.updateUser(newuser);
		gameRepo.saveInGameUser(uge);
	}

	public void saveGame(GameEntity ge){
		gameRepo.saveGame(ge);
	}

	public List<GameEntity> searchGames(int p,int max){
		return gameRepo.queryGames(p,max);
	}

	public GameEntity getGame(int id){
		return gameRepo.findById(id);
	}
	

}
