package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;


public class CreateFile {

    private static TextField textField = new TextField();
    private static File createdFile;

    public static void display(String title, String directoryName) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(250);
        window.setMinWidth(250);
        Button enter = new Button("Enter");
        enter.setOnAction(event -> {
            if (textField.getText() != null) {
                File newF = new File(directoryName + "//" + textField.getText());
                try {
                    newF.createNewFile();
                    createdFile = newF;
                } catch (Exception e) {
                    System.out.println(e);
                }
                textField.clear();
                window.close();
            }
        });
        Label label = new Label("File Name");
        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, textField, enter);
        layout.setAlignment(Pos.TOP_LEFT);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

    public static File getCreatedFile() {
        return (createdFile);
    }
}
