package com.joedoe.bullshitbingo.alarmdevice.mqtt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MqttUtil {

	public final static String SERVER_URL_SUFFIX = ".messaging.internetofthings.ibmcloud.com:1883";
	public final static String DEVICE_TYPE = "bsb-alarm";
	public final static String CMD_ID_START_ALARM = "startalarm";
	public final static String CMD_ID_STOP_ALARM = "stopalarm";

	public final static String ORG_KEY = "org";
	public final static String DEVICE_ID_KEY = "deviceId";
	public final static String AUTH_TOKEN_KEY = "authToken";
	public final static String API_KEY = "apiKey";
	
	public static Properties readProperties(String filePath) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}
}
