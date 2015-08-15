package com.joedoe.bullshitbingo.recorder.core;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.joedoe.bullshitbingo.recorder.model.Recording;

public class SpeechToTextTransformer implements Runnable {

	private final static Duration WAIT_FOR_FILES_DURATION = Duration
			.ofSeconds(10);

	private boolean started = false;

	private CountDownLatch cdlStart;

	private CountDownLatch cdlStop;
	
	private SpeechToText speechToTextService;

	public SpeechToTextTransformer() {
		cdlStart = new CountDownLatch(1);
		cdlStop = new CountDownLatch(1);
		speechToTextService = new SpeechToText();
		speechToTextService.setUsernameAndPassword("5acdc459-6670-4dd6-a6e6-2d519f1bd26f", "JIiPogFQZLaS");
	}

	@Override
	public void run() {
		// wait before starting
		try {
			cdlStart.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// read files+transform them
		while (started) {
			File recording = readNextFile();
			if (recording == null) {
				waitForRecording();
			} else {
				transformSpeechToText(recording);
			}
		}
		cdlStop.countDown();
	}

	private void transformSpeechToText(File recordingFile) {
		System.out.println("processing recording " + recordingFile.getName());
		Map<String, Object> params = new HashMap<>();
		params.put("audio", recordingFile);
		params.put("content_type", "audio/wav");
		params.put("model", "en-US_BroadbandModel");
		params.put("continuous", "true");
		params.put("max_alternatives", "1");
		params.put("inactivity_timeout", "-1");
		SpeechResults transcript = speechToTextService.recognize(params);
		Recording recording = transformRecording(recordingFile.getName(), transcript);
		System.out.println(recording);	
		System.gc();
		recordingFile.delete();
	}

	private Recording transformRecording(String name, SpeechResults transcript) {
		String id = name.substring(0, name.length()-4);
		Recording returnValue = new Recording(id);
		for (Transcript t : transcript.getResults()) 
			for (SpeechAlternative sa : t.getAlternatives()) 
				returnValue.addWord(sa.getTranscript().trim());			
		return returnValue;
	}

	private void waitForRecording() {
		try {
			Thread.sleep(WAIT_FOR_FILES_DURATION.toMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private File readNextFile() {
		// get all files
		List<File> allRecordings = new ArrayList<File>();
		for (final File fileEntry : new File(Recorder.RECORDING_DIR)
				.listFiles())
			allRecordings.add(fileEntry);
		// only start transforming if there is more then one file (as i gets deleted)
		if (allRecordings.size() <= 1) return null;
		// sort by name
		allRecordings.sort(new Comparator<File>()  {
			@Override
			public int compare(File o1, File o2) {
				String fileName1 = o1.getName();
				String fileName2 = o2.getName();
				return fileName1.compareTo(fileName2);
			}
		});
		return allRecordings.get(0);
	}

	public synchronized void stop() throws InterruptedException {
		if (!started)
			return;
		started = false;
		cdlStop.await();
	}

	public synchronized void start() {
		if (started)
			return;
		cdlStart = new CountDownLatch(1);
		cdlStop = new CountDownLatch(1);
		started = true;
		new Thread(null, this, "speech-to-text-thread").start();
		cdlStart.countDown();
	}

}
