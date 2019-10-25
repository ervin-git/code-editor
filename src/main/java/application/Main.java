
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class Main extends Application {

    // FIXME: sometimes the file contents get copied and saved multiple times?
    // TODO: compile, execute

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("new_code_editor.fxml")));
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Scene scene = new Scene(root, (double) width / 2, (double) height / 2);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("java-keywords.css")).toExternalForm());

        primaryStage.setTitle("Avengers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}