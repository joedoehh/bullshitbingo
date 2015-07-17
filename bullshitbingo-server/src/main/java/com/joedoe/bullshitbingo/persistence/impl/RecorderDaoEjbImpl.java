package com.joedoe.bullshitbingo.persistence.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import org.lightcouch.CouchDatabase;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.NoDocumentException;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.common.config.BullshitBingoConfiguration;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Stateless
public class RecorderDaoEjbImpl implements RecorderDao {


	@Override
	public @NotNull List<RecordingBo> findAll() {
		CouchDbProperties cdbprops = new CouchDbProperties();
		cdbprops.setHost("8b75c387-e78f-4c81-a16f-257261fd9570-bluemix.cloudant.com");
		cdbprops.setUsername("8b75c387-e78f-4c81-a16f-257261fd9570-bluemix");
		cdbprops.setPassword("7fcafb47d751fdef1f02ed6a3a6dd8c653a12da7354431ad568e4066d963c312");
		CouchDbClient cdbc = new CouchDbClient(new CouchDbProperties());
		CouchDatabase db = cdbc.database("testdb", true);

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
		return null;
	}

	@Override
	public RecordingBo create(@NotNull RecordingBo recordingBo) {
		return null;
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
	}
	
}
