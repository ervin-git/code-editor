package application.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


public class StatsController implements Initializable {

    private HashMap<String, Integer> dic;

    // Text Area
    @FXML
    private TextArea text;

    // Other
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    public StatsController(HashMap<String, Integer> dic) {
        this.dic = dic;
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (String key : dic.keySet()) {
            int value = dic.get(key);
            text.append(key + ": " + value + "\n");
        }
    }
}
