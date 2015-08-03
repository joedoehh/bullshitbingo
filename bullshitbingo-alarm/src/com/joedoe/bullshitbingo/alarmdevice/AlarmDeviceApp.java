package com.joedoe.bullshitbingo.alarmdevice;

import java.io.IOException;

import com.joedoe.bullshitbingo.alarmdevice.view.AlarmDeviceController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AlarmDeviceApp extends Application {

	private Stage primaryStage;
	private AnchorPane alarmDevicePane;
	private AlarmDeviceController alarmDeviceController;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Alarm Device");
		this.primaryStage.getIcons().add(
				new Image(getClass().getResource("notification-bell.png")
						.toString()));
		initLayout();
		primaryStage.setOnCloseRequest(eventHandler -> {
			alarmDeviceController.handleDisconnect();
			Platform.exit();
		});
	}

	private void initLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AlarmDeviceApp.class
					.getResource("view/AlarmDevice.fxml"));
			alarmDevicePane = (AnchorPane) loader.load();
			Scene scene = new Scene(alarmDevicePane);
			primaryStage.setScene(scene);
			primaryStage.show();
			alarmDeviceController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
