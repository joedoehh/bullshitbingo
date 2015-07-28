package com.joedoe.bullshitbingo.persistence.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.persistence.api.RecorderDao;

@Stateless
public class RecorderDaoEjbImpl implements RecorderDao {

	@PersistenceContext(unitName = "bsbPersistenceUnit")
	private EntityManager em;

	public RecorderDaoEjbImpl() {
	}

	@Override
	public @NotNull List<RecordingBo> findAll() {
		return em
				.createNamedQuery("listRecordings", RecordingBo.class)
				.getResultList();
	}

	@Override
	public @NotNull List<RecordingBo> findAfterTimestamp(
			@NotNull Date lastRecordingTimestamp) {
		Preconditions.checkNotNull(lastRecordingTimestamp);
		return em.createNamedQuery("listRecordingsAfterRecordingTimestamp",
						RecordingBo.class)
				.setParameter("timestampOfRecording",
						lastRecordingTimestamp.getTime()).getResultList();
	}

	@Override
	public RecordingBo findById(@NotNull Long id) {
		Preconditions.checkNotNull(id);
		return em.find(RecordingBo.class, id);
	}

	@Override
	public RecordingBo create(@NotNull RecordingBo recordingBo) {
		Preconditions.checkNotNull(recordingBo);
		em.persist(recordingBo);
		return recordingBo;
	}

	@Override
	public void deleteAll() {
		 em.createNamedQuery("deleteRecordings")
				.executeUpdate();
	}

	@Override
	public void delete(@NotNull Long id) {		
		Preconditions.checkNotNull(id);
		em.createNamedQuery("deleteRecordingById")
		.setParameter("id", id)
		.executeUpdate();
	}

}