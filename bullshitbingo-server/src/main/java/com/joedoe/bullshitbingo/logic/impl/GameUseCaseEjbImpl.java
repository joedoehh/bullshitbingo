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
		return gameStateEjb.getState();
	}

	@Override
	public void joinGame(@NotNull Game game) {
		Preconditions.checkNotNull(game);
		gameStateEjb.joinGame(game);		
	}

}