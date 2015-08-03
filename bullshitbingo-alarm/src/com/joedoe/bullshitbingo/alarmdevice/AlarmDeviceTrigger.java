package com.joedoe.bullshitbingo.alarmdevice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

import com.joedoe.bullshitbingo.alarmdevice.mqtt.MqttHandler;
import com.joedoe.bullshitbingo.alarmdevice.mqtt.MqttUtil;

public class AlarmDeviceTrigger {

	public static void main(String[] args) {
		new AlarmDeviceTrigger().start();
	}

	private void start() {
		// Read properties from the conf file

		// get connection parameter
		Properties props = MqttUtil
				.readProperties("conf/alarmDeviceTriggerApp.conf");
		String org = props.getProperty(MqttUtil.ORG_KEY);
		String device_id = props.getProperty(MqttUtil.DEVICE_ID_KEY);
		String apiKey = props.getProperty(MqttUtil.API_KEY);
		String authtoken = props.getProperty(MqttUtil.AUTH_TOKEN_KEY);

		System.out.println("org: " + org);
		System.out.println("apiKey: " + apiKey);
		System.out.println("authtoken" + authtoken);

		// Format: a:<orgid>:<app-id>
		String clientId = "a:" + org + ":" + apiKey;
		String serverHost = org + MqttUtil.SERVER_URL_SUFFIX;

		AppMqttHandler handler = new AppMqttHandler(device_id);
		handler.connect(serverHost, clientId, apiKey, authtoken);
		for (int i = 0; i < 5; i++) {
			handler.doStartAlarm("Whoo Hoo " + i);
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handler.doStopAlarm();
		}
		handler.disconnect();
	}

	private class AppMqttHandler extends MqttHandler {
		
		private String deviceId;
		
		public AppMqttHandler(String deviceId) {
			this.deviceId = deviceId;
		}

		public void doStartAlarm(String alarmText) {
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("cmd", MqttUtil.CMD_ID_START_ALARM);
				jsonObj.put("text", alarmText);
				jsonObj.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			publish("iot-2/type/" + MqttUtil.DEVICE_TYPE + "/id/"
					+ deviceId + "/cmd/" + MqttUtil.CMD_ID_START_ALARM
					+ "/fmt/json", jsonObj.toString(), false, 0);
		}

		public void doStopAlarm() {
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("cmd", MqttUtil.CMD_ID_STOP_ALARM);
				jsonObj.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			publish("iot-2/type/" + MqttUtil.DEVICE_TYPE + "/id/"
					+ deviceId + "/cmd/" + MqttUtil.CMD_ID_STOP_ALARM
					+ "/fmt/json", jsonObj.toString(), false, 0);

		}

	}

}
