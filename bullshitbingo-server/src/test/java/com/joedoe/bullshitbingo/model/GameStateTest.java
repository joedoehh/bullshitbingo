package com.joedoe.bullshitbingo.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.joedoe.bullshitbingo.model.GameState.StateEnum;

public class GameStateTest {

	private GameState gameState;

	@Before
	public void setupEmptyGame() {
		List<MarkedWord> markedWords = new ArrayList<MarkedWord>();
		List<Game> games = new ArrayList<Game>();
		gameState = new GameState(StateEnum.RUNNING, markedWords, games);
	}

	@Test
	public void testMatchingWord() {
		addGame(gameState, "player-1", new String[] { "aaa", "bbb", "x1", "x2",
				"x3" });
		addGame(gameState, "player-2", new String[] { "bbb", "ccc", "x1", "x2",
				"x3" });
		addGame(gameState, "player-3",
				new String[] { "ddd", "x1", "x2", "x3", "x4" });
		List<RecordingBo> recordings = createRecordings(new String[] { "bbb",
				"ccc" }, new String[] { "eee" });
		gameState.processRecordings(recordings);
		assertThat(gameState.getState(), equalTo(StateEnum.RUNNING));
		assertThat(gameState.getWinner(), is(nullValue()));
		assertThat(getGame(gameState, "player-1").getRating(), equalTo(1));
		assertThat(getGame(gameState, "player-2").getRating(), equalTo(2));
		assertThat(getGame(gameState, "player-3").getRating(), equalTo(0));
	}

	@Test
	public void testFindWinner() {
		addGame(gameState, "player-1", new String[] { "a", "b", "c", "d",
				"e" });
		List<RecordingBo> recordings = createRecordings(new String[] { "a",
				"b" }, new String[] { "c", "d", "e" });
		gameState.processRecordings(recordings);
		assertThat(gameState.getState(), equalTo(StateEnum.FINISHED_WITH_WINNER));
		assertThat(gameState.getWinner(), equalTo("player-1"));
	}

	@Test
	public void testFindWinners() {
		addGame(gameState, "player-1", new String[] { "a", "b", "c", "d",
				"e" });
		addGame(gameState, "player-2", new String[] { "a", "b", "c", "d",
		"e" });
		addGame(gameState, "player-3", new String[] { "a", "a2", "b", "d",
		"e" });
		List<RecordingBo> recordings = createRecordings(new String[] { "a",
				"b" }, new String[] { "c", "d", "e" });
		gameState.processRecordings(recordings);
		assertThat(gameState.getState(), equalTo(StateEnum.FINISHED_WITH_WINNER));
		assertThat(gameState.getWinner(), equalTo("player-1 player-2"));
	}

	@Test
	public void testMatchingWordsOfLastRecording() {
		addGame(gameState, "player-1", new String[] { "aaa", "bbb", "x1", "x2",
				"x3" });
		List<RecordingBo> recordings = createRecordings(new String[] { "y" },
				new String[] { "aaa", "y", "bbb" });
		gameState.processRecordings(recordings);
		assertThat(
				gameState.getLastMatchedRecording(),
				containsInAnyOrder(new MarkedWord("aaa", true), new MarkedWord(
						"y"), new MarkedWord("bbb", true)));
	}

	private Game getGame(GameState gameState, String player) {
		for (Game game : gameState.getGames())
			if (game.getPlayer().equals(player))
				return game;
		return null;
	}

	private List<RecordingBo> createRecordings(String[] words1, String[] words2) {
		List<RecordingBo> returnValue = new ArrayList<RecordingBo>();
		List<String> wordsList = new ArrayList<String>();
		for (String word : words1)
			wordsList.add(word);
		RecordingBo recording = new RecordingBo();
		recording.setWords(wordsList);
		returnValue.add(recording);

		wordsList = new ArrayList<String>();
		for (String word : words2)
			wordsList.add(word);
		recording = new RecordingBo();
		recording.setWords(wordsList);
		returnValue.add(recording);
		return returnValue;
	}

	private void addGame(GameState gameState, String player, String[] words) {
		List<String> wordsList = new ArrayList<String>(words.length);
		for (String word : words)
			wordsList.add(word);
		Game game = new Game(player, wordsList);
		gameState.addGame(game);
	}

}
