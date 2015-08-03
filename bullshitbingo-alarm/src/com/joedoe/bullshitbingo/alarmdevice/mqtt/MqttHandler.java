package com.joedoe.bullshitbingo.alarmdevice.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MqttHandler implements MqttCallback {

	private MqttClient client = null;

	public MqttHandler() {

	}

	@Override
	public void connectionLost(Throwable throwable) {
		if (throwable != null) {
			throwable.printStackTrace();
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage)
			throws Exception {
		String payload = new String(mqttMessage.getPayload());
		System.out.println(".messageArrived - Message received on topic "
				+ topic + ": message is " + payload);
	}

	public void connect(String brokerUrl, String clientId, String userName,
			String authtoken) {
		// check if client is already connected
		if (!isMqttConnected()) {
			String connectionUri = "tcp://" + brokerUrl;			

			if (client != null) {
				try {
					client.disconnect();
				} catch (MqttException e) {
					e.printStackTrace();
				}
				client = null;
			}

			try {
				client = new MqttClient(connectionUri, clientId);
			} catch (MqttException e) {
				e.printStackTrace();
			}

			client.setCallback(this);

			// create MqttConnectOptions and set the clean session flag
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName(userName);
			options.setPassword(authtoken.toCharArray());

			try {
				// connect
				client.connect(options);
				System.out.println("Connected to " + connectionUri);
			} catch (MqttException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Disconnect MqttClient from the MQTT server
	 */
	public void disconnect() {

		// check if client is actually connected
		if (isMqttConnected()) {
			try {
				// disconnect
				client.disconnect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Subscribe MqttClient to a topic
	 * 
	 * @param topic
	 *            to subscribe to
	 * @param qos
	 *            to subscribe with
	 */
	public void subscribe(String topic, int qos) {

		// check if client is connected
		if (isMqttConnected()) {
			try {
				client.subscribe(topic, qos);
				System.out.println("Subscribed: " + topic);

			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			connectionLost(null);
		}
	}

	/**
	 * Unsubscribe MqttClient from a topic
	 * 
	 * @param topic
	 *            to unsubscribe from
	 */
	public void unsubscribe(String topic) {
		// check if client is connected
		if (isMqttConnected()) {
			try {

				client.unsubscribe(topic);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			connectionLost(null);
		}
	}

	/**
	 * Publish message to a topic
	 * 
	 * @param topic
	 *            to publish the message to
	 * @param message
	 *            JSON object representation as a string
	 * @param retained
	 *            true if retained flag is requred
	 * @param qos
	 *            quality of service (0, 1, 2)
	 */
	public void publish(String topic, String message, boolean retained, int qos) {
		System.out.println("topic="+topic);
		System.out.println("message="+message);
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
			connectionLost(null);
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

}
