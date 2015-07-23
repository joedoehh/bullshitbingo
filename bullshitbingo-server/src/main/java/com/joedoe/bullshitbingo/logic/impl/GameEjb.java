package com.joedoe.bullshitbingo.logic.impl;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.model.Game;
import com.joedoe.bullshitbingo.model.GameState;
import com.joedoe.bullshitbingo.model.GameState.StateEnum;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Startup
@Singleton
public class GameEjb {
	
	private static final int TIMEOUT_REPEAT_GAME = 5*1000;

	private static final int TIMEOUT_BEFORE_START_OF_GAME_MS = 1000;

	private Timer timer;
	
	private GameState gameState = new GameState();
	
	private Date lastRecordingTimestamp = new Date();
	
	@EJB
	private RecorderDao recorderDao; 
	
	public synchronized GameState getGameState() {
		return gameState;
	}

	public synchronized GameState.StateEnum getState() {
		return gameState.getState();
	}
	
	public void setGameLifcycleState(@NotNull GameState.StateEnum newState) {
		Preconditions.checkNotNull(newState);
		if (GameState.StateEnum.RUNNING == newState) {
			gameState.setState(StateEnum.RUNNING);	
		} else if (GameState.StateEnum.STOPPED == newState) {
			gameState.setState(StateEnum.STOPPED);
		} else if (GameState.StateEnum.INITIALIZED == newState) {
			gameState = new GameState();
		} else {
			throw new IllegalArgumentException("no operation defined to set the game into state " + newState);
		}		
	}
	
	public synchronized void joinGame(@NotNull Game game) {
		Preconditions.checkNotNull(game);
		gameState.addGame(game);		
	}
	

    private void doWork(){
    	// check if game is running or not
    	if (GameState.StateEnum.RUNNING != gameState.getState()) {
    		log("game state is not " + GameState.StateEnum.RUNNING + ", nothing to do");
    		return;
    	}
    	
        // fetch recordings to process
		log("looking for recordings after " + lastRecordingTimestamp);
    	List<RecordingBo> recordings = recorderDao.findAfterTimestamp(lastRecordingTimestamp);
    	lastRecordingTimestamp = calculateNextTimestamp(recordings);
    	if (recordings.size() == 0) {
    		log("found no new recordings");
    		return;
    	}
    	
    	// play the game
    	synchronized (this) {
    		log("processing recordings " + recordings);
			gameState.processRecordings(recordings);
			if (GameState.StateEnum.FINISHED == gameState.getState())
				// TODO not just print the result ?!?!?
				System.out.println("we have a winner " + gameState.getWinner());
		}
    }

	
	private Date calculateNextTimestamp(List<RecordingBo> recordings) {
		Date returnValue = lastRecordingTimestamp;
		for (RecordingBo recording : recordings)
			if (recording.getTimestampOfRecordingAsDate().after(returnValue))
				returnValue = recording.getTimestampOfRecordingAsDate(); 
		return returnValue;
	}

	@PostConstruct
	private void startTimer() {
		timer = new Timer("bsb-gamethread");
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				doWork();
			}
			
		}, TIMEOUT_BEFORE_START_OF_GAME_MS, TIMEOUT_REPEAT_GAME);
	}

	@PreDestroy
	private void stopTimer() {
		timer.cancel();
		timer = null;
	}
	
	private void log(String text) {
		System.out.println(new Date() + " : " + text);
	}

}