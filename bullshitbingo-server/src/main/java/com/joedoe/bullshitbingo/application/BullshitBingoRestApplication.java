package com.joedoe.bullshitbingo.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/bsb/*")
public class BullshitBingoRestApplication extends Application 
{

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		classes.add(com.joedoe.bullshitbingo.service.impl.RecorderServiceRestImpl.class);
		classes.add(com.joedoe.bullshitbingo.service.impl.GameServiceRestImpl.class);
		return classes;
	}

}
