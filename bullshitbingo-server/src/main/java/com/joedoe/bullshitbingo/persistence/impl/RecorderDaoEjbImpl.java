package com.joedoe.bullshitbingo.persistence.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.Resource.AuthenticationType;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Stateless
@Resource(name = "jdbc/db2DataSource", type = javax.sql.DataSource.class, shareable = true, authenticationType = AuthenticationType.CONTAINER)
public class RecorderDaoEjbImpl implements RecorderDao {

	@PersistenceUnit(unitName = "bullshitbingoServerPeristenceUnit")
	private EntityManagerFactory emf;

	public RecorderDaoEjbImpl() {
	}

	@Override
	public @NotNull List<RecordingBo> findAll() {
		return emf.createEntityManager()
				.createNamedQuery("listRecordings", RecordingBo.class)
				.getResultList();
	}

	@Override
	public @NotNull List<RecordingBo> findAfterTimestamp(
			@NotNull Date lastRecordingTimestamp) {
		Preconditions.checkNotNull(lastRecordingTimestamp);
		return emf
				.createEntityManager()
				.createNamedQuery("listRecordingsAfterRecordingTimestamp",
						RecordingBo.class)
				.setParameter("timestampOfRecording",
						lastRecordingTimestamp.getTime()).getResultList();
	}

	@Override
	public RecordingBo findById(@NotNull Long id) {
		Preconditions.checkNotNull(id);
		return emf.createEntityManager().find(RecordingBo.class, id);
	}

	@Override
	public RecordingBo create(@NotNull RecordingBo recordingBo) {
		Preconditions.checkNotNull(recordingBo);
		emf.createEntityManager().persist(recordingBo);
		return recordingBo;
	}

	@Override
	public void deleteAll() {
		 emf.createEntityManager()
				.createNamedQuery("deleteRecordings")
				.executeUpdate();
	}

	@Override
	public void delete(@NotNull Long id) {		
		Preconditions.checkNotNull(id);
		emf
		.createEntityManager()
		.createNamedQuery("deleteRecordingById")
		.setParameter("id", id)
		.executeUpdate();
	}

}