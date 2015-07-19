package com.joedoe.bullshitbingo.service.impl;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.common.config.BullshitBingoConfiguration;
import com.joedoe.bullshitbingo.logic.api.RecorderUseCase;
import com.joedoe.bullshitbingo.model.RecordingBo;
import com.joedoe.bullshitbingo.model.RecordingTo;
import com.joedoe.bullshitbingo.service.api.RecorderService;

@Path("/recording")
public class RecorderServiceRestImpl implements RecorderService {

	private RecorderUseCase recorderUsecase;

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public RecordingTo createRecording(
			@NotNull @QueryParam("word") List<String> words) {
		Preconditions.checkNotNull(words);
		RecordingBo bo = getRecorderUseCase().createRecording(words);
		return new RecordingTo(bo);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public @NotNull List<RecordingTo> getRecordings() {
		List<RecordingTo> returnValue = RecordingTo
				.transferToTo(getRecorderUseCase().getRecordings());
		return returnValue;
	}

	@GET
	@Path("{recordingId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public @NotNull RecordingTo getRecording(
			@NotNull @PathParam("recordingId") Long recordingId) {
		Preconditions.checkNotNull(recordingId);
		RecordingBo bo = getRecorderUseCase().getRecording(recordingId);
		if (null == bo)
			return null;
		else
			return new RecordingTo(bo);
	}

	@DELETE
	@Override
	public void deleteAllRecordings() {
		getRecorderUseCase().deleteAllRecordings();
	}

	@DELETE
	@Path("{recordingId}")
	@Override
	public void deleteRecording(
			@NotNull @PathParam("recordingId") Long recordingId) {
		Preconditions.checkNotNull(recordingId);
		getRecorderUseCase().deleteRecording(recordingId);
	}

	private RecorderUseCase getRecorderUseCase() {
		if (null == recorderUsecase) {
			String beanName = BullshitBingoConfiguration
					.getRecorderUsecaseBeanName();
			Preconditions.checkNotNull(beanName);
			recorderUsecase = (RecorderUseCase) lookupLocalBean(beanName);
			Preconditions.checkNotNull(recorderUsecase);
		}
		return recorderUsecase;
	}

	private Object lookupLocalBean(String name) {
		Object returnValue = null;
		try {
			InitialContext context = new InitialContext();
			returnValue = context.lookup(name);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

}