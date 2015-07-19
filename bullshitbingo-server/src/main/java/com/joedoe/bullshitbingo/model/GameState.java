package com.joedoe.bullshitbingo.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

public class GameState {

	public enum StateEnum {
		INITIALIZED, RUNNING, FINISHED_STOPPED, FINISHED_WITH_WINNER
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

	public GameState(@NotNull StateEnum state,
			@NotNull List<MarkedWord> lastMatchedRecording,
			@NotNull List<Game> games) {
		Preconditions.checkNotNull(state);
		Preconditions.checkNotNull(lastMatchedRecording);
		Preconditions.checkNotNull(games);
		this.state = state;
		this.lastMatchedRecording = lastMatchedRecording;
		this.games = games;
	}

	public void processRecordings(@NotNull List<RecordingBo> recordings) {
		Preconditions.checkNotNull(state);
		for (RecordingBo recording : recordings) {
			if (StateEnum.FINISHED_WITH_WINNER == state)
				break;
			processRecoding(recording);
		}
	}

	private void processRecoding(@NotNull RecordingBo recording) {
		Preconditions.checkNotNull(state);
		lastMatchedRecording = new ArrayList<MarkedWord>();
		// iterate over the words and check if they match to users participating
		for (String nextWord : recording.getWords()) {
			// get users with a match
			List<Game> matchingGames = matchGamesWithWord(games, nextWord);
			if (matchingGames.size() > 0) {
				// evaluate winners
				lastMatchedRecording.add(new MarkedWord(nextWord, true));
				winner = evaluateWinners(matchingGames);
				if (null != winner) {
					state = StateEnum.FINISHED_WITH_WINNER;
					break;
				}
			} else {
				// save last processed word unmarked
				lastMatchedRecording.add(new MarkedWord(nextWord));
			}
		}
	}

	private String evaluateWinners(List<Game> matchingGames) {
		String returnValue = null;
		for (Game game : matchingGames)
			if (game.isWon()) {
				if (null == returnValue)
					returnValue = game.getPlayer();
				else
					returnValue += " " + game.getPlayer() + " ";
			}
		returnValue = (null == returnValue) ? null : returnValue.trim();
		return returnValue;
	}

	private List<Game> matchGamesWithWord(List<Game> gamesToFilter,
			String wordToMatch) {
		List<Game> gamesWithAMatch = new ArrayList<Game>();
		for (Game game : gamesToFilter)
			for (MarkedWord markedWord : game.getWords())
				if (!markedWord.isMarked()
						&& markedWord.getWord().equals(wordToMatch)) {
					// mark word + increase hit count
					markedWord.setMarked(true);
					gamesWithAMatch.add(game);
					game.increaseRating();
					break;
				}
		return gamesWithAMatch;
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

}