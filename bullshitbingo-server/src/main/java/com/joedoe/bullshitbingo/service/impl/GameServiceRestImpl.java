package com.joedoe.bullshitbingo.service.impl;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.common.config.BullshitBingoConfiguration;
import com.joedoe.bullshitbingo.logic.api.GameUseCase;
import com.joedoe.bullshitbingo.model.Game;
import com.joedoe.bullshitbingo.model.GameState;
import com.joedoe.bullshitbingo.service.api.GameService;

@Path("/game")
public class GameServiceRestImpl implements GameService {

	private GameUseCase gameUsecase;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public @NotNull GameState getState() {
		return getGameUseCase().getState();
	}

	@PUT
	@Override
	public void setGameState(
			@QueryParam("newGameState") @NotNull GameState.StateEnum state) {
		Preconditions.checkNotNull(state);
		getGameUseCase().setGameLifcycleState(state);
	}

	@PUT
	@Override
	public void joinGame(
			@QueryParam("player") @NotNull String player,
			@QueryParam("word") @NotNull @Size(min = 5, max = 5) List<String> words) {
		Preconditions.checkNotNull(player);
		Preconditions.checkArgument(words.size() == 5);
		getGameUseCase().joinGame(new Game(player, words));
	}

	private GameUseCase getGameUseCase() {
		if (null == gameUsecase) {
			String beanName = BullshitBingoConfiguration
					.getGameUsecaseBeanName();
			Preconditions.checkNotNull(beanName);
			gameUsecase = (GameUseCase) lookupLocalBean(beanName);
			Preconditions.checkNotNull(gameUsecase);
		}
		return gameUsecase;
	}

	private Object lookupLocalBean(String name) {
		Object returnValue = null;
		try {
			InitialContext context = new InitialContext();
			returnValue = context.lookup(name);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

}