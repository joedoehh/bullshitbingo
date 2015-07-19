package com.joedoe.bullshitbingo.logic.api;

import javax.validation.constraints.NotNull;

import com.joedoe.bullshitbingo.model.Game;
import com.joedoe.bullshitbingo.model.GameState;

public interface GameUseCase {

	void setGameLifcycleState(@NotNull GameState.StateEnum newState); 
	
	@NotNull
	GameState getState();

	void joinGame(@NotNull Game game);
	
}
