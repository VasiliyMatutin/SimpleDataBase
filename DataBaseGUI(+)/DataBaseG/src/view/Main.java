package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage=primaryStage;
		this.primaryStage.setTitle("XantusTables");
		loadMainMenu();
		rootLayout.setRight(null);
		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			controller.exit();
		});
	}

	public void loadMainMenu() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("MainMenu.fxml"));
			rootLayout = (BorderPane) loader.load();
			controller = loader.getController();
			controller.setMain(this);
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public BorderPane getRootLayout() {
		return rootLayout;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
