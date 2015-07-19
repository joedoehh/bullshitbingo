package com.joedoe.bullshitbingo.service.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.joedoe.bullshitbingo.model.RecordingTo;

public interface RecorderService {
		
	// null return vlaue == error in creating
	RecordingTo createRecording(@NotNull List<String> words);
	
	// ordered by timestamp
	@NotNull List<RecordingTo> getRecordings();

	@NotNull RecordingTo getRecording(@NotNull Long recordingId);
	
	void deleteAllRecordings();
	
	void deleteRecording(@NotNull Long recordingId);

}
