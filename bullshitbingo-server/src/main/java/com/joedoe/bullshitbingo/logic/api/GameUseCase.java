package com.joedoe.bullshitbingo.logic.api;

import javax.validation.constraints.NotNull;

import com.joedoe.bullshitbingo.model.Game;
import com.joedoe.bullshitbingo.model.GameState;

public interface GameUseCase {

	void startGame();
	
	void finishGame();
	
	void resetGame();
	
	@NotNull
	GameState getState();

	void joinGame(@NotNull Game game);
	
}
