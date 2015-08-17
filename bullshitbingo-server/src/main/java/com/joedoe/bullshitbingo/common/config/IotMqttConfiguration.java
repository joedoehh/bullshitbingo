package com.joedoe.bullshitbingo.common.config;


public class IotMqttConfiguration {	
	
	public String org;
	public String apiKey;
	public String authtoken;
	
	@Override
	public String toString() {
		return "IotMqttConfiguration [org=" + org + ", apiKey=" + apiKey
				+ ", authtoken=" + authtoken + "]";
	}	
	
}
