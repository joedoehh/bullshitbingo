package com.joedoe.bullshitbingo.logic.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.joedoe.bullshitbingo.model.RecordingBo;

public interface RecorderUseCase {

	// null return vlaue == error in creating
	RecordingBo createRecording(@NotNull List<String> words);
	
	// ordered by timestamp
	@NotNull List<RecordingBo> getRecordings();

	@NotNull RecordingBo getRecording(@NotNull String id);
	
	void deleteRecording(@NotNull String id);
	
	void deleteAllRecordings();
	
}
