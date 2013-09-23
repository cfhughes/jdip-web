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

	@Transactional
	public void addUserToGame(GameEntity game, UserEntity user){
		if (game.getStage() != Stage.PREGAME){
			return;
		}
		if (gameRepo.inGameUser(game.getId(), user.getId()) != null){
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

		gameRepo.updateGame(game);
		userRepo.updateUser(user);
		//Apparently, hibernate does this already
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
