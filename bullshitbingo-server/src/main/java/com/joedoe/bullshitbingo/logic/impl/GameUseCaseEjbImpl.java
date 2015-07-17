package com.joedoe.bullshitbingo.logic.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.joedoe.bullshitbingo.logic.api.GameUseCase;
import com.joedoe.bullshitbingo.model.Game;
import com.joedoe.bullshitbingo.model.GameState;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Stateless
public class GameUseCaseEjbImpl implements GameUseCase {

	@EJB
	private RecorderDao recorderDao;
	
	@EJB
	private GameEjb gameStateEjb;
	

	@Override
	public void startGame() {
		gameStateEjb.startGame();
	}

	@Override
	public void finishGame() {
		gameStateEjb.finishGame();
	}

	@Override
	public void resetGame() {
		gameStateEjb.resetGame();
	}

	@Override
	public GameState getState() {
		return gameStateEjb.getState();
	}

	@Override
	public void joinGame(Game game) {
		gameStateEjb.joinGame(game);		
	}

}