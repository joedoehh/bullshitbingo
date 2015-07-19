package com.joedoe.bullshitbingo.persistence.api;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.joedoe.bullshitbingo.model.RecordingBo;

public interface RecorderDao {

	@NotNull List<RecordingBo> findAll();

	@NotNull List<RecordingBo> findAfterTimestamp(@NotNull Date lastRecordingTimestamp);
	
	RecordingBo findById(@NotNull Long id);
	
	// null return value == error in creating
	RecordingBo create(@NotNull RecordingBo recordingBo);
	
	void deleteAll();

	void delete(@NotNull Long id);
	
}
