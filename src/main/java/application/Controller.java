package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem close;

    @FXML
    private MenuItem create;

    @FXML
    private MenuItem open;

    @FXML
    private MenuItem save;

    @FXML
    private TextField createText;

    @FXML
    private TextArea codeArea;

    @FXML
    private TreeView<String> tree;

    @FXML
    void close(ActionEvent event) {
        codeArea.clear();
        tree.setRoot(null);
    }

    @FXML
    public void create(ActionEvent event) {
        create.setOnAction(e -> Create.display("Create"));
    }

    @FXML
    void open(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        loadTree(selectedDirectory, null);
    }

    private void loadTree(File dir, TreeItem<String> parent) {
        TreeItem<String> root = new TreeItem<>(dir.getName());
        root.setExpanded(true);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                loadTree(file, root);
            else
                root.getChildren().add(new TreeItem<>(file.getName()));
        }

        if (parent == null)
            tree.setRoot(root);
        else
            parent.getChildren().add(root);

        root.setExpanded(false);
    }

    @FXML
    void save(ActionEvent event) {
    }

    @FXML
    void initialize() {
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'codeeditor.fxml'.";
        assert create != null : "fx:id=\"create\" was not injected: check your FXML file 'codeeditor.fxml'.";
        assert open != null : "fx:id=\"open\" was not injected: check your FXML file 'codeeditor.fxml'.";
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'codeeditor.fxml'.";
    }

}

