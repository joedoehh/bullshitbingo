import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class IotConfigurationTest {

	public static void main(String[] args) {
		try {
			// device client id format: d:<org-id>:<type-id>:<device-id>
			MqttClient client = new MqttClient(
					"tcp://jubcru.messaging.internetofthings.ibmcloud.com:1883",
					"d:jubcru:bsb-alarm:bsb-alarm-001"); 
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName("use-token-auth");
			options.setPassword("DPb1l6tD2d?+uu9sGr".toCharArray());
			client.connect(options);
			System.out.println("Connected");
		} catch (MqttException me) {
			me.printStackTrace();					
		}				
		
	}

}
