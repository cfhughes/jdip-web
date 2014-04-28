package com.chughes.dip.game;

import java.util.List;
import java.util.Set;

import javax.activity.InvalidActivityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chughes.dip.controller.HomeController;
import com.chughes.dip.data.GameRepository;
import com.chughes.dip.data.UserRepository;
import com.chughes.dip.game.GameEntity.Stage;
import com.chughes.dip.user.UserEntity;

@Service
public class GameService {

	@Autowired private GameRepository gameRepo;
	@Autowired private UserRepository userRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	public void addUserToGame(GameEntity game, UserEntity user, String secret) throws Exception{
		if (game.getStage() != Stage.PREGAME){
			throw new Exception("Something went wrong, the game has allready started.");
		}
		if (gameRepo.inGameUser(game.getId(), user.getId()) != null){
			throw new Exception("The user is already in this game.");
		}
		if (game.getSecret() != null && game.getSecret().length() > 0 && !game.getSecret().equals(secret)) {
			throw new Exception("Incorrect password");
		}
		if (game.getLevel() != 0 && game.getLevel() != user.getLevel()){
			throw new Exception("User is not the correct level for this game.");
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

	public List<GameEntity> searchGames(int p,int max, Integer j){
		return gameRepo.queryGames(p,max,j);
	}

	public GameEntity getGame(int id){
		return gameRepo.findById(id);
	}
	

}
