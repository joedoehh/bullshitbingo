package com.joedoe.bullshitbingo.alarmdevice.mqtt;

import javafx.application.Platform;

import org.apache.commons.json.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.joedoe.bullshitbingo.alarmdevice.view.AlarmDeviceController;

public class AlarmDeviceMqttHandler extends MqttHandler {

	private AlarmDeviceController alarmDeviceController;
	
	public AlarmDeviceMqttHandler(AlarmDeviceController alarmDeviceController) {
		this.alarmDeviceController = alarmDeviceController;
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage)
			throws Exception {
		super.messageArrived(topic, mqttMessage);	
		String payload = new String(mqttMessage.getPayload());
		alarmDeviceController.appendToProtocol("< "+topic+" : " + payload);
		// Execute the commands received
		if (topic.equals("iot-2/cmd/" + MqttUtil.CMD_ID_START_ALARM
				+ "/fmt/json")) {			
			JSONObject jsonObject = new JSONObject(payload);
			String text = jsonObject.getString("text");            
			// start the alarm with given text			
            Platform.runLater(() -> alarmDeviceController.handleTurnOnAlarm(text));			
		}
		else if (topic.equals("iot-2/cmd/" + MqttUtil.CMD_ID_STOP_ALARM
				+ "/fmt/json")) {
			// stop the alarm
			Platform.runLater(() -> alarmDeviceController.handleTurnOffAlarm());
		}
	}	
}
