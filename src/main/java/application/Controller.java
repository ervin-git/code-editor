package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


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
    private TreeView<File> treeView;


    @FXML
    void close(ActionEvent event) {
    }

    @FXML
    public void create(ActionEvent event) {
        create.setOnAction(e -> Create.display("Create"));
    }

    @FXML
    void open(ActionEvent event) throws IOException {
        FileChooser directoryChooser = new FileChooser();
        File selectedDirectory = directoryChooser.showOpenDialog(new Stage());
        if (selectedDirectory != null) {
            javafx.application.Platform.runLater( () -> {
                try {
                    Scanner s = new Scanner(selectedDirectory);
                    while (s.hasNextLine()) {
                        codeArea.appendText(s.nextLine());
                    }
                } catch (FileNotFoundException | NullPointerException ex) {
                    System.err.println(ex);
                }
            });
        }
    }

    private void findFiles(File dir, TreeItem<File> parent) {
        TreeItem<File> root = new TreeItem<>(dir);
        root.setExpanded(true);
        try {
            File[] files = dir.listFiles();
            System.out.println(dir.listFiles());
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    findFiles(file, root);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                    root.getChildren().add(new TreeItem<>(file));
                }
            }
            if (parent == null) {
                treeView.setRoot(root);
            } else {
                parent.getChildren().add(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        assert treeView != null;

        findFiles(new File("src/"), null);
    }
}

