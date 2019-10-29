package application;

import application.controllers.StatsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Stats {
    private HashMap<String, Integer> dic = new HashMap<>();

    public Stats(String str) {
        computeWordCount(str);
    }

    private void computeWordCount(String str) {
        // TOOD: logic
        // https://codereview.stackexchange.com/questions/29091/number-of-occurrences-of-each-different-word-in-a-line-of-text
        for (String word : str.split(" "))
            // If key doesn't exists set default value to 1 otherwise take sum of the current value plus 1
            dic.merge(word, 1, Integer::sum);
    }

    public void displayStats() {
        //TODO: new window, to display all the stats?
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            StatsController statsController = new StatsController(dic);

            fxmlLoader.setLocation(getClass().getClassLoader().getResource("stats_page.fxml"));
            fxmlLoader.setController(statsController);

            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Statistics");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
