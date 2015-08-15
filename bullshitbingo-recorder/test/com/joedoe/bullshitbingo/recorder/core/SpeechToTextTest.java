package com.joedoe.bullshitbingo.recorder.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class SpeechToTextTest {

	public static void main(String[] args) {
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("5acdc459-6670-4dd6-a6e6-2d519f1bd26f", "JIiPogFQZLaS");
		// File audio = new File("C:\\tmp\\recording\\little-brown-fox.wav");
		File audio = new File("C:\\tmp\\recording\\one-two-three.wav");	
		Map<String, Object> params = new HashMap<>();
		params.put("audio", audio);
		params.put("content_type", "audio/wav");
		params.put("model", "en-US_BroadbandModel");
		params.put("continuous", "true");
		params.put("max_alternatives", "1");
		params.put("inactivity_timeout", "-1");
		SpeechResults transcript = service.recognize(params);
		System.out.println(transcript);	
		
	}

}
