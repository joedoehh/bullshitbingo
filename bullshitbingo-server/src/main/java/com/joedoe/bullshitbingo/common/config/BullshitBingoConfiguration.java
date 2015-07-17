package com.joedoe.bullshitbingo.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.base.Preconditions;

public class BullshitBingoConfiguration {

	private static final String VCAP_APP_CONFIG_FILENAME = "localhost-bullshitBingoServer_VCAP_AppConfig.json";
	private static final String VCAP_APP_CONFIG_ENV_KEY = "VCAP_APP_CONFIG";
	private static final String VCAP_SERVICES_FILENAME = "localhost-bullshitBingoServer_VCAP_Services.json";
	private static final String VCAP_SERVICES_ENV_KEY = "VCAP_SERVICES";
	;

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

	public static String getRecorderDatabaseName() {
		initialize();
		return (String) vcapAppConfig.get("recorderDbName");
	}	

	public static String getAllRecordingViewName() {
		initialize();
		return (String) vcapAppConfig.get("recorderDbName.allRecordingView");
	}	
	
	public static String getCloudantUsername() {
		return (String) extractCloudantCredentials().get("username");
	}

	public static String getCloudantPassword() {
		return (String) extractCloudantCredentials().get("password");
	}

	public static String getCloudantUrl() {
		return (String) extractCloudantCredentials().get("url");
	}

	private static JSONObject extractCloudantCredentials() {
		initialize();
		JSONArray cloudantArray = (JSONArray) vcapServices
				.get("cloudantNoSQLDB");
		JSONObject firstEntry = (JSONObject) cloudantArray.get(0);
		JSONObject credentitals = (JSONObject) firstEntry.get("credentials");
		return credentitals;
	}

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
		// 1. read from environment
		String returnValue = System.getenv(VCAP_SERVICES_ENV_KEY);
		if (null == returnValue) {
			// 2. read from path
			Path path = FileSystems.getDefault().getPath(filename);
			try {
				returnValue = new String(Files.readAllBytes(path));	
			} catch (NoSuchFileException nsfe) {
				// 3. read file from classpath
				InputStream inputStream = BullshitBingoConfiguration.class.getClassLoader()
                        .getResourceAsStream(filename);
				returnValue = getStringFromInputStream(inputStream);
			}
			
		}
		Preconditions.checkNotNull("not found conf in env with key " + envName
				+ " or as file " + filename + " in default file system or from class loader", returnValue);
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
