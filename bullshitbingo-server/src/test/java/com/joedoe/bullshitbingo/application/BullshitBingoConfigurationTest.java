package com.joedoe.bullshitbingo.application;

import junit.framework.Assert;

import org.junit.Test;

import com.joedoe.bullshitbingo.common.config.BullshitBingoConfiguration;

public class BullshitBingoConfigurationTest {

	@Test
	public void testParsingRecorderUsecaseBeanName() {
		Assert.assertEquals(
				"java:global/bullshitbingo-server/RecorderUseCaseEjbImpl!com.joedoe.bullshitbingo.logic.api.RecorderUseCase",
				BullshitBingoConfiguration.getRecorderUsecaseBeanName());
	}

}
