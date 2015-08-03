package com.joedoe.bullshitbingo.alarmdevice.view;

import java.net.URL;
import java.util.Properties;

import com.joedoe.bullshitbingo.alarmdevice.mqtt.AlarmDeviceMqttHandler;
import com.joedoe.bullshitbingo.alarmdevice.mqtt.MqttUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AlarmDeviceController {

	@FXML
	private Label label;
	
	@FXML
	private ImageView imageView;
	
	@FXML
	private TextArea textArea;	
	
	private AlarmDeviceMqttHandler alarmDeviceMqttHandler = new AlarmDeviceMqttHandler(this);
	
	private MediaPlayer mediaPlayer;
	
	@FXML
	private void initialize() {
		textArea.setEditable(false);
		URL alarmFile = getClass().getResource("Annoying_Alarm_Clock.mp3");
		Media alarmSound = new Media(alarmFile.toString());
		mediaPlayer = new MediaPlayer(alarmSound);
		appendToProtocol("Alarm Device Initialized");
		handleTurnOffAlarm();
	}

	public void handleTurnOnAlarm(String text) {
		imageView.setImage(new Image(getClass().getResource("alarm.png").toString()));
		label.setText(text);
		mediaPlayer.play();
	}	
	
	@FXML
	private void handleTurnOnAlarm() {
		handleTurnOnAlarm("");
	}
	
	@FXML
	public void handleTurnOffAlarm() {
		label.setText("");
		imageView.setImage(null);
		mediaPlayer.stop();
	}
	
	public void appendToProtocol(String string) {
		textArea.appendText(string);
		textArea.appendText("\n");
	}
	
	@FXML
	private void handleConnect() {
		// get connection parameter
		Properties props = MqttUtil.readProperties("conf/alarmDevice.conf");
		String org = props.getProperty(MqttUtil.ORG_KEY);
		String device_id = props.getProperty(MqttUtil.DEVICE_ID_KEY);
		String device_type = MqttUtil.DEVICE_TYPE;
		String username = "use-token-auth";
		String authtoken = props.getProperty(MqttUtil.AUTH_TOKEN_KEY);
		String serverHost = org + MqttUtil.SERVER_URL_SUFFIX;
		String clientId = "d:" + org + ":" + device_type + ":" + device_id;

		// connect + subscribe for commands to be handled
		alarmDeviceMqttHandler.connect(serverHost, clientId, username, authtoken);	
		appendToProtocol("connecting with client-id '"+clientId+"' to server host '"+serverHost+"'");
		
		//iot-2/cmd/<cmd-type>/fmt/<format-id>
		subscribeCmd("iot-2/cmd/" + MqttUtil.CMD_ID_START_ALARM + "/fmt/json");
		subscribeCmd("iot-2/cmd/" + MqttUtil.CMD_ID_STOP_ALARM + "/fmt/json");
	}

	private void subscribeCmd(String command) {
		alarmDeviceMqttHandler.subscribe(command, 0);
		appendToProtocol("subscribed to command '"+command+"'");		
	}
	
	@FXML
	public void handleDisconnect() {
		alarmDeviceMqttHandler.disconnect();
		appendToProtocol("disconnected");
	}	
}
