package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller {
    private File activeFile;

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
        activeFile = directoryChooser.showDialog(new Stage());
        loadTree(activeFile, null);
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

        tree.getSelectionModel().selectedItemProperty()
                .addListener((observable, old_val, new_val) -> {
                    StringBuilder pathBuilder = new StringBuilder();
                    for (TreeItem<String> item = tree.getSelectionModel().getSelectedItem();
                         item != null; item = item.getParent()) {
                        if (item.getParent() != null) {
                            pathBuilder.insert(0, item.getValue());
                            pathBuilder.insert(0, "/");
                        }
                    }
                    String path = activeFile.getPath() + pathBuilder.toString();
                    File file = new File(path);
                    if(!file.isDirectory()) {
                        javafx.application.Platform.runLater(() -> {
                            try {
                                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                                while (bufferedReader.readLine() != null)
                                    codeArea.appendText(bufferedReader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        codeArea.setStyle("-fx-font-family: monospace");
                    }
                });
    }

}

