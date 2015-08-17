package com.joedoe.bullshitbingo.logic.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.validation.constraints.NotNull;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.simple.JSONObject;

import com.google.common.base.Preconditions;
import com.joedoe.bullshitbingo.common.config.BullshitBingoConfiguration;
import com.joedoe.bullshitbingo.common.config.IotMqttConfiguration;

@Startup
@Singleton
public class IotNotificationEjb {

	private final static String DEVICE_TYPE = "bsb-alarm";
	private final static String DEVICE_ID = "bsb-alarm-001";
	private final static String CMD_START_ALARM = "startalarm";
	private final static String CMD_STOP_ALARM = "stopalarm";
	private final static String IOT_BROKER_URL = "tcp://jubcru.messaging.internetofthings.ibmcloud.com:1883";

	private MqttClient client = null;
	private IotMqttConfiguration iotConfig;

	public IotNotificationEjb() {
		iotConfig = BullshitBingoConfiguration.getIotMqttConfiguration();
	}

	public void doStartAlarm(@NotNull String alarmText) {
		Preconditions.checkNotNull(alarmText);
		if (!isMqttConnected()) {
			log("not connected, swallowing to start alarm with text "
					+ alarmText);
			return;
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cmd", CMD_START_ALARM);
		jsonObj.put("text", alarmText);
		jsonObj.put("time",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String command = "iot-2/type/" + DEVICE_TYPE + "/id/" + DEVICE_ID + "/cmd/"
				+ CMD_START_ALARM + "/fmt/json";
		publish(command, jsonObj.toString(), false, 0);
	}

	public void doStopAlarm() {
		if (!isMqttConnected()) {
			log("not connected, swallowing to stop alarm");
			return;
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cmd", CMD_STOP_ALARM);
		jsonObj.put("time",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String command = "iot-2/type/" + DEVICE_TYPE + "/id/" + DEVICE_ID + "/cmd/"
				+ CMD_STOP_ALARM + "/fmt/json";
		publish(command, jsonObj.toString(), false, 0);
	}

	// TODO joedoe activate when iot available
	// @PostConstruct
	private void connectToIot() throws RuntimeException {
		if (!isMqttConnected()) {			
			String clientId = "a:" + iotConfig.org + ":" + iotConfig.apiKey;
			if (client != null) {
				try {
					client.disconnect();
				} catch (Exception e) {
					client = null;
				}
			}

			try {
				client = new MqttClient(IOT_BROKER_URL, clientId);
				MqttConnectOptions options = new MqttConnectOptions();
				options.setCleanSession(true);
				options.setUserName(iotConfig.apiKey);
				options.setPassword(iotConfig.authtoken.toCharArray());

				log("Connecting to " + IOT_BROKER_URL + " with client id "
						+ clientId + ", username " + iotConfig.apiKey
						+ " and password " + iotConfig.authtoken);
				client.connect(options);
				log("connection successfull");
			} catch (MqttException e) {
				client = null;
				throw new RuntimeException(e);
			} 
		}

	}

	// TODO joedoe activate when iot available
	// @PreDestroy
	private void disconnectFromIot() throws RuntimeException {
		if (isMqttConnected()) {
			try {
				client.disconnect();
			} catch (MqttException e) {
				throw new RuntimeException(e);
			} finally {
				client = null;
			}
		}
	}

	private void publish(String topic, String message, boolean retained, int qos) {
		log("topic=" + topic + ", message=" + message);
		// check if client is connected
		if (isMqttConnected()) {
			// create a new MqttMessage from the message string
			MqttMessage mqttMsg = new MqttMessage(message.getBytes());
			// set retained flag
			mqttMsg.setRetained(retained);
			// set quality of service
			mqttMsg.setQos(qos);
			try {
				client.publish(topic, mqttMsg);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			log("swallowed as not connected");
		}
	}

	private boolean isMqttConnected() {
		boolean connected = false;
		try {
			if ((client != null) && (client.isConnected())) {
				connected = true;
			}
		} catch (Exception e) {
			// swallowing the exception as it means the client is not connected
		}
		return connected;
	}

	private void log(String text) {
		System.out.println(new Date() + " : " + text);
	}

}