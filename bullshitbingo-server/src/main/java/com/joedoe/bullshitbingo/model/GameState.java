package com.joedoe.bullshitbingo.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

public class GameState {

	public enum StateEnum {
		INITIALIZED,
		RUNNING,
		FINISHED,
		FINISHED_WITH_WINNER
	}
	
	@NotNull
	private StateEnum state;

	private String winner;
	
	@NotNull	
	private List<MarkedWord> lastMatchedRecording;
	
	@NotNull		
	private List<Game> games;

	public GameState() {
		this.state = StateEnum.INITIALIZED;
		this.winner = null;
		this.lastMatchedRecording = new ArrayList<>();
		this.games = new ArrayList<>();
	}
	
	public GameState(@NotNull StateEnum state, @NotNull List<MarkedWord> lastMatchedRecording,
			@NotNull List<Game> games) {
		Preconditions.checkNotNull(state);
		Preconditions.checkNotNull(lastMatchedRecording);		
		Preconditions.checkNotNull(games);
		this.state = state;
		this.lastMatchedRecording = lastMatchedRecording;
		this.games = games;
	}

	public StateEnum getState() {
		return state;
	}

	public void setState(StateEnum newState) {
		state = newState;
	}	
	
	public String getWinner() {
		return winner;
	}
		
	public void setWinner(String winner) {
		this.winner = winner;
	}

	public List<MarkedWord> getLastMatchedRecording() {
		return lastMatchedRecording;
	}

	public List<Game> getGames() {
		return games;
	}

	public void addGame(@NotNull Game game) {
		Preconditions.checkNotNull(game);
		games.add(game);
	}	
	
	@Override
	public String toString() {
		final int maxLen = 10;
		return "GameState [state="
				+ state
				+ ", winner="
				+ winner
				+ ", lastMatchedRecording="
				+ (lastMatchedRecording != null ? lastMatchedRecording.subList(
						0, Math.min(lastMatchedRecording.size(), maxLen))
						: null)
				+ ", games="
				+ (games != null ? games.subList(0,
						Math.min(games.size(), maxLen)) : null) + "]";
	}

	public void processRecordings(List<RecordingBo> recordings) {
		// TODO Auto-generated method stub
		
	}
		
}