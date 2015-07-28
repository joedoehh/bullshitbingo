package com.joedoe.bullshitbingo.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.base.Preconditions;

public class BullshitBingoConfiguration {

	private static final String VCAP_APP_CONFIG_FILENAME = "localhost-bullshitBingoServer_VCAP_AppConfig.json";
	private static final String VCAP_APP_CONFIG_ENV_KEY = "VCAP_APP_CONFIG";
	private static final String VCAP_SERVICES_FILENAME = "localhost-bullshitBingoServer_VCAP_Services.json";
	private static final String VCAP_SERVICES_ENV_KEY = "VCAP_SERVICES";

	private static JSONObject vcapServices;
	private static JSONObject vcapAppConfig;
	private static boolean initialized = false;

	public static String getGameUsecaseBeanName() {
		initialize();
		return (String) vcapAppConfig.get("gameUsecaseBeanName");
	}	
	
	public static String getRecorderUsecaseBeanName() {
		initialize();
		return (String) vcapAppConfig.get("recorderUsecaseBeanName");
	}

	public static IotMqttConfiguration getIotMqttConfiguration() {
		initialize();
		IotMqttConfiguration returnValue = new IotMqttConfiguration();
		Object o = vcapServices.get("iotf-service");
		System.out.println("iotf-service="+o);
		return returnValue;
	}
	
//	   "iotf-service": [
//	                    {
//	                       "name": "bullshitbingo-iot",
//	                       "label": "iotf-service",
//	                       "plan": "iotf-service-free",
//	                       "credentials": {
//	                          "iotCredentialsIdentifier": "a2g6k39sl6r5",
//	                          "mqtt_host": "y5kewl.messaging.internetofthings.ibmcloud.com",
//	                          "mqtt_u_port": 1883,
//	                          "mqtt_s_port": 8883,
//	                          "base_uri": "https://y5kewl.internetofthings.ibmcloud.com:443/api/v0001",
//	                          "http_host": "y5kewl.internetofthings.ibmcloud.com",
//	                          "org": "y5kewl",
//	                          "apiKey": "a-y5kewl-7pvzji5miq",
//	                          "apiToken": "tljpyA?oj?Hmx7L)7m"
//	                       }
//	                    }
//	                 ],	
	
	private static void initialize() {
		if (initialized)
			return;
		JSONParser parser = new JSONParser();
		String vcapString;
		try {
			vcapString = getConfig(VCAP_SERVICES_ENV_KEY,
					VCAP_SERVICES_FILENAME);
			vcapServices = (JSONObject) parser.parse(vcapString);
			vcapString = getConfig(VCAP_APP_CONFIG_ENV_KEY,
					VCAP_APP_CONFIG_FILENAME);
			vcapAppConfig = (JSONObject) parser.parse(vcapString);
			initialized = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getConfig(String envName, String filename)
			throws Exception {
		System.out.println("getConfig(): looking up " + envName + " in " + filename);
		// 1. read from environment
		String returnValue = System.getenv(envName);
		if (null == returnValue) {
			System.out.println("not found in environment, looking in filesystem");
			// 2. read from path
			Path path = FileSystems.getDefault().getPath(filename);
			try {
				returnValue = new String(Files.readAllBytes(path));	
			} catch (NoSuchFileException nsfe) {
				System.out.println("not found in path, looking in classpath");
				// 3. read file from classpath
				InputStream inputStream = BullshitBingoConfiguration.class.getClassLoader()
                        .getResourceAsStream(filename);
				returnValue = getStringFromInputStream(inputStream);
			}
			
		}
		Preconditions.checkNotNull("not found conf in env with key " + envName
				+ " or as file " + filename + " in default file system or from class loader", returnValue);
		System.out.println("getConfig(): returnValue=" + returnValue);
		return returnValue;
	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}

}
