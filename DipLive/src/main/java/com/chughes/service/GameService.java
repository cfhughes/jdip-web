package com.chughes.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chughes.data.GameRepository;
import com.chughes.dip.GameEntity;
import com.chughes.dip.UserGameEntity;
import com.chughes.security.UserDAO;
import com.chughes.security.UserEntity;

@Service
public class GameService {

	@Autowired private GameRepository gameRepo;
	@Autowired private UserDAO userRepo;
	
	public void addUserToGame(GameEntity game, UserEntity user){
		UserGameEntity uge = new UserGameEntity();
		uge.setGame(game);
		uge.setUser(user);
		//TODO: Some sort of power selection
		uge.setPower("England");
		Set<UserGameEntity> players = game.getPlayers();
		players.add(uge);
		game.setPlayers(players);
		user.addGame(uge);
		
		gameRepo.updateGame(game);
		userRepo.updateUser(user);
		//gameRepo.saveInGameUser(uge);
	}
	
	public void saveGame(GameEntity ge){
		gameRepo.saveGame(ge);
	}
}
