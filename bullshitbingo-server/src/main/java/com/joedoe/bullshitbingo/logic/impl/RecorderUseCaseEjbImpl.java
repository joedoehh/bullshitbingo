package com.joedoe.bullshitbingo.logic.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.logic.api.RecorderUseCase;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Stateless
public class RecorderUseCaseEjbImpl implements RecorderUseCase {

	@EJB
	private RecorderDao recorderDao;
	
	@Override
	public RecordingBo createRecording(@NotNull List<String> words) {
		Preconditions.checkNotNull(words);		
		return recorderDao.create(new RecordingBo(words));		
	}

	@Override
	public @NotNull List<RecordingBo> getRecordings() {
		List<RecordingBo> returnValue = recorderDao.findAll();
		returnValue.sort(RecordingBo.COMPARATOR_BY_TIMESTAMP_ASC);
		return returnValue;
	}

	@Override
	public @NotNull RecordingBo getRecording(@NotNull String id) {
		Preconditions.checkNotNull(id);					
		return recorderDao.findById(id);
	}

	@Override
	public void deleteAllRecordings() {
		recorderDao.deleteAll();
	}

	@Override
	public void deleteRecording(@NotNull String id) {
		Preconditions.checkNotNull(id);	
		recorderDao.delete(id);
	}

}
