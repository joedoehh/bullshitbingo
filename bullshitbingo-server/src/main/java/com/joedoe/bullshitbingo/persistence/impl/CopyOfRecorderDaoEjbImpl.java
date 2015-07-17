package com.joedoe.bullshitbingo.persistence.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import org.lightcouch.NoDocumentException;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.View;
import com.cloudant.client.api.model.Response;
import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.common.config.BullshitBingoConfiguration;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;


public class CopyOfRecorderDaoEjbImpl implements RecorderDao {

	private Database cloudAntRecordingDb;
	private String recordingDbName;
	// private String allRecordingViewName;

	@Override
	public @NotNull List<RecordingBo> findAll() {
		// View allRecordingsView = cloudAntRecordingDb.view(allRecordingViewName);
		RecordingBo returnValue = cloudAntRecordingDb.findAny(RecordingBo.class, "");
		return new ArrayList<>();
	}

	@Override
	public @NotNull List<RecordingBo> findAfterTimestamp(@NotNull Date lastRecordingTimestamp) {
		Preconditions.checkNotNull(lastRecordingTimestamp);
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public RecordingBo findById(@NotNull String id) {
		Preconditions.checkNotNull(id);
		RecordingBo returnValue;
		try {
			returnValue = cloudAntRecordingDb.find(RecordingBo.class, id);
		} catch (NoDocumentException nde) {
			returnValue = null;
		}
		return returnValue;
	}

	@Override
	public RecordingBo create(@NotNull RecordingBo recordingBo) {
		Preconditions.checkNotNull(recordingBo);
		Response response = cloudAntRecordingDb.post(recordingBo);
		if (null == response.getError()) {
			recordingBo.set_id(response.getId());
			recordingBo.set_rev(response.getRev());
		} else {
			recordingBo = null;
		}
		return recordingBo;
	}

	@Override
	public void deleteAll() {
		for (RecordingBo bo : findAll())
			delete(bo);
	}

	@Override
	public void delete(@NotNull String id) {
		Preconditions.checkNotNull(id);
		delete(findById(id));
	}

	private void delete(RecordingBo recordingBo) {
		if (null == recordingBo)
			return;
		cloudAntRecordingDb.remove(recordingBo);
	}
	
	@PostConstruct
	private void initialize() {
		CloudantClient cloudAntDbClient = new CloudantClient(
				BullshitBingoConfiguration.getCloudantUrl(),
				BullshitBingoConfiguration.getCloudantUsername(),
				BullshitBingoConfiguration.getCloudantPassword());
		recordingDbName = BullshitBingoConfiguration.getRecorderDatabaseName();
//		allRecordingViewName = BullshitBingoConfiguration
//				.getAllRecordingViewName();
		cloudAntRecordingDb = cloudAntDbClient.database(recordingDbName, true);
	}

}
