package test;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogBox {
    public static void display(String title, String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(400);
        stage.setMinHeight(200);
        stage.setTitle(title);

        Label label = new Label();
        label.setText(message);

        Button button = new Button("OK");
        button.setOnAction(e -> stage.close());

        VBox layout = new VBox();
        layout.getChildren().addAll(label,button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage.setScene(scene);

        stage.showAndWait();
    }

}
