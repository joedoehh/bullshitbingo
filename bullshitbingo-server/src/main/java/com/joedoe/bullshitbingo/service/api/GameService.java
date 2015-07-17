package com.joedoe.bullshitbingo.service.api;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.joedoe.bullshitbingo.model.GameState;

public interface GameService {

	@NotNull
	GameState getState();

	void setGameState(@NotNull GameState.StateEnum state);

	void joinGame(@NotNull String player,
			@NotNull @Size(min = 5, max = 5) List<String> words);

}
