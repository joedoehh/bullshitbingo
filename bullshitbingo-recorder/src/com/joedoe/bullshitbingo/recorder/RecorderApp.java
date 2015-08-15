package com.joedoe.bullshitbingo.recorder;

import java.io.IOException;



import com.joedoe.bullshitbingo.recorder.view.RecorderController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RecorderApp extends Application {

	private Stage primaryStage;
	private AnchorPane recorderPane;
	private RecorderController recorderController;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Recorder");
		this.primaryStage.getIcons().add(
				new Image(getClass().getResource("black_8_audio_micro.png")
						.toString()));
		initLayout();
		primaryStage.setOnCloseRequest(eventHandler -> {
			recorderController.handleApplicationClose();
			Platform.exit();
		});
	}

	private void initLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RecorderApp.class
					.getResource("view/Recorder.fxml"));
			recorderPane = (AnchorPane) loader.load();
			Scene scene = new Scene(recorderPane);
			primaryStage.setScene(scene);
			primaryStage.show();
			recorderController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
