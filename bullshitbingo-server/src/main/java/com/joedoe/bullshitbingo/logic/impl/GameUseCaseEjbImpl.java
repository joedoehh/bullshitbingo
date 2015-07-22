package com.joedoe.bullshitbingo.logic.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.logic.api.GameUseCase;
import com.joedoe.bullshitbingo.model.Game;
import com.joedoe.bullshitbingo.model.GameState;
import com.joedoe.bullshitbingo.model.GameState.StateEnum;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Stateless
public class GameUseCaseEjbImpl implements GameUseCase {

	@EJB
	private RecorderDao recorderDao;

	@EJB
	private GameEjb gameStateEjb;

	@Override
	public void setGameLifcycleState(@NotNull StateEnum newState) {
		Preconditions.checkNotNull(newState);
		gameStateEjb.setGameLifcycleState(newState);
	}

	@Override
	public GameState getState() {
		return gameStateEjb.getGameState();
	}

	@Override
	public void joinGame(@NotNull Game game) {
		Preconditions.checkNotNull(game);
		boolean gameIsInitialized = gameStateEjb.getState().equals(GameState.StateEnum.INITIALIZED); 
		boolean gameIsRunning = gameStateEjb.getState().equals(GameState.StateEnum.RUNNING);
		Preconditions.checkState(gameIsInitialized || gameIsRunning,
				"to join a game it must be in state "
						+ GameState.StateEnum.INITIALIZED + " or "
						+ GameState.StateEnum.RUNNING
						+ " but current state is "
						+ gameStateEjb.getState());
		gameStateEjb.joinGame(game);
	}

}