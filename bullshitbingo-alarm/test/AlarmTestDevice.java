import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class AlarmTestDevice {

	public static void main(String[] args) {
		try {							
			MqttClient client = new MqttClient(
					"tcp://jubcru.messaging.internetofthings.ibmcloud.com:1883",
					"d:jubcru:bsb-alarm:bsb-alarm-001"); // d:<org-id>:<type-id>:<device-id>
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName("use-token-auth"); // also tried "token"
			options.setPassword("DPb1l6tD2d?+uu9sGr".toCharArray());
			client.connect(options);
			System.out.println("Connected");
		} catch (MqttException me) {
			me.printStackTrace();					
		}				
		
	}

}
