package com.joedoe.bullshitbingo.recorder.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.joedoe.bullshitbingo.recorder.core.Recorder;
import com.joedoe.bullshitbingo.recorder.core.SpeechToTextTransformer;

public class RecorderController {

	@FXML
	private ImageView imageView;

	@FXML
	private TextArea textArea;

	@FXML
	private Button recordingButton;

	@FXML
	private Slider slider;

	private Image recordingImage;

	private boolean recordingOn = false;

	private Recorder recorder = new Recorder();

	private SpeechToTextTransformer speechToTextTransformer = new SpeechToTextTransformer();

	@FXML
	private void initialize() {
		textArea.setEditable(false);
		imageView.setImage(null);
		String imageResourceClass = getClass().getResource("recording.jpg")
				.toString();
		recordingImage = new Image(imageResourceClass);
	}

	@FXML
	private void handleRecording() {
		if (recordingOn) {
			recordingOn = false;
			imageView.setImage(null);
			recordingButton.setText("Start Recording");
			appendToProtocol("... recording stopped");
			try {
				recorder.stop();
				speechToTextTransformer.stop();				
			} catch (InterruptedException e) {
				appendToProtocol("error stopping recorder");
				e.printStackTrace();
			}
		} else {
			recordingOn = true;
			imageView.setImage(recordingImage);
			recordingButton.setText("Stop Recording");
			int timesInSeconds = new Double(slider.getValue()).intValue();
			appendToProtocol("start recording (" + timesInSeconds
					+ "s chunks)...");
			recorder.start(timesInSeconds);
			speechToTextTransformer.start();
		}
	}

	@FXML
	private void handleClear() {
	}

	public void appendToProtocol(String string) {
		textArea.appendText(string);
		textArea.appendText("\n");
	}

	public void handleApplicationClose() {
		try {
			recorder.stop();
			speechToTextTransformer.stop();				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
