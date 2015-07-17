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
	
	@Test
	public void testCloudantUsername() {
		Assert.assertEquals(
				"8b75c387-e78f-4c81-a16f-257261fd9570-bluemix",
				BullshitBingoConfiguration.getCloudantUsername());
	}
	
}
