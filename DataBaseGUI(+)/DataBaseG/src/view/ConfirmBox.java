package view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean answer;
    static boolean cancelled;
    
    private ConfirmBox(){
	}

    public static boolean display(String title, String message) {
    	answer = true;
    	cancelled = false;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        
        window.setOnCloseRequest(e -> {
        	cancelled = true;
        	 window.close();
        });
        
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(yesButton, noButton);
        hBox.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(5);
        layout.getChildren().addAll(label, hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
    
    public static boolean isCancelled() {
    	return cancelled;
    }

}
