package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;

public class Stats {
    private HashMap<String, Integer> dic = new HashMap<>();

    public Stats(String str) {
        computeWordCount(str);
    }

    private void computeWordCount(String str) {
        for (String word : str.split(" "))
            // If key doesn't exists set default value to
            // 1 otherwise take sum of the current value plus 1
            dic.merge(word, 1, Integer::sum);
    }

    public void displayStats() {
        Stage window = new Stage();
        TextArea textArea = new TextArea();
        VBox vBox = new VBox();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Statistics");
        window.setMinHeight(250);
        window.setMinWidth(250);

        textArea.appendText("[Word]: [Number of appearances]\n");
        for (String key : dic.keySet()) {
            int value = dic.get(key);
            if (key.equals("\n"))
                textArea.appendText("<EOL>: " + value + "\n");
            else
                textArea.appendText(key + ": " + value + "\n");
        }

        vBox.getChildren().add(textArea);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }
}
