package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReadBox {

	private static String temp;
	private static Boolean main_flag;
	
	private ReadBox(){
	}
	
    public static String display(String title, String inputText) {
    	temp="";
    	main_flag=true;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Button addButton = new Button(title);
        
        Text outputText = new Text(inputText);
        
        TextField nameInput = new TextField();
        nameInput.setMinWidth(100);
        
        addButton.setOnAction(e -> {
        	temp = nameInput.getText();
            window.close();
            main_flag=false;
        });
        
        nameInput.setOnKeyReleased(event -> {
        	  if (event.getCode() == KeyCode.ENTER){
        		  addButton.fire();
        	  }
        	});

        VBox layout = new VBox(0);

        layout.getChildren().addAll(outputText,nameInput, addButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return temp;
    }
    
    public static boolean isClosedManually() {
    	return main_flag;
    }

}
