package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;


public class Create {

    public static TextField textField = new TextField();
    public static TextField textField2 = new TextField();

    public static void display(String title) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(250);
        window.setMinWidth(250);
        Button enter = new Button("Enter");
        enter.setOnAction(event -> {
            if (textField.getText() != null && textField2.getText() != null) {
                StringBuffer sBuffer = new StringBuffer();
                sBuffer.append(textField2.getText()).append("/").append(textField.getText());
                textField.clear();
                textField2.clear();
                File file = new File(sBuffer.toString());
                if (!file.exists()) {
                    boolean result = file.mkdir();
                }

                window.close();
            }
        });
        Button lookup = new Button("Lookup");
        lookup.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(new Stage());
            textField2.setText(selectedDirectory.getAbsoluteFile().toString());
        });
        Label label = new Label("Project Name");
        Label label2 = new Label("Location");
        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, textField, label2, textField2, lookup, enter);
        layout.setAlignment(Pos.TOP_LEFT);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}