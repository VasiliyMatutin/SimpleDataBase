package view;

import view.AlertBox;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MultipleReadBox {

	private static String temp;
	private static Boolean main_flag;

	private MultipleReadBox(){
	}

	public static ArrayList<String> display(String title, String inputText, Boolean repetitionsAlowed, String key) {
		ArrayList<String> temp_array = new ArrayList<String>();
		main_flag=true;
		temp_array.add(key);
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);

		Button saveButton = new Button("Save");
		Button addButton = new Button("Add...");

		Text outputText = new Text(inputText);

		TextField nameInput = new TextField();
		nameInput.setMinWidth(100);

		addButton.setOnAction(e -> {
			temp = nameInput.getText();
			if (temp.isEmpty()==true){
				AlertBox.display("Error", "Empty field!");
			}
			else{
				if (repetitionsAlowed == false){
					for (String column_name : temp_array){
						if (column_name.equals(temp)){
							AlertBox.display("Error", "Such name already exists!");
							nameInput.clear();
							return;
						}
					}
				}
				temp_array.add(temp);
				nameInput.clear();
			}
		});

		nameInput.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER){
				addButton.fire();
			}
		});

		saveButton.setOnAction(e -> {
			window.close();
			main_flag=false;
		});

		window.setOnCloseRequest(e -> {
			e.consume();
			Boolean answer = ConfirmBox.display("Exit", "Unsaved changes! Sure you want to exit?");
			if (answer == true){
				window.close();
			}
		});

		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10,10, 10, 10));
		hBox.setSpacing(10);
		hBox.getChildren().addAll(nameInput, addButton);
		hBox.setAlignment(Pos.CENTER);

		VBox layout = new VBox(0);
		layout.getChildren().addAll(outputText, hBox, saveButton);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

		return temp_array;
	}

	public static boolean isClosedManually() {
		return main_flag;
	}
}
