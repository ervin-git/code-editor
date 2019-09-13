package application;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;


public class Controller {

    private File activeDir, activeFile;

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
        tree.setRoot(null);
        loadTree(activeDir, null);
    }

    @FXML
    void open(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        activeDir = directoryChooser.showDialog(new Stage());
        loadTree(activeDir, null);
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
        ObservableList<CharSequence> paragraph = codeArea.getParagraphs();
        Iterator<CharSequence> iter = paragraph.iterator();
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(activeFile));
            while (iter.hasNext()) {
                CharSequence seq = iter.next();
                bf.append(seq);
                bf.newLine();
            }
            bf.flush();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    String path = activeDir.getPath() + pathBuilder.toString();
                    activeFile = new File(path);
                    if (!activeFile.isDirectory()) {
                        javafx.application.Platform.runLater(() -> {
                            try {
                                BufferedReader bufferedReader = new BufferedReader(new FileReader(activeFile));
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

