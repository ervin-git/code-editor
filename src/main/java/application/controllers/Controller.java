package application.controllers;


import application.Create;
import application.CreateFile;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;


public class Controller implements Initializable {


    private File activeDir, activeFile;
    private TreeItem<String> currRoot;
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
    private TextArea codeArea;
    private TextFlow codeArea;
    @FXML
    private TreeView<String> tree;
    @FXML
    private MenuItem saveFile;
    @FXML
    private MenuItem openFile;
    @FXML
    private MenuItem closeFile;
    @FXML
    private MenuItem createFile;
    @FXML
    private MenuItem removeFile;
    @FXML
    private Label activeFileName;

    //File Functions
    //
    @FXML
    void removeFile(ActionEvent event) {
        //Find the current File from the TreeView then remove it from the tree view
        if (activeFile != null) {
            String name = getNameOf(activeFile.toString());
            File toDelete = activeFile;
            for (int i = 0; i < currRoot.getChildren().size(); i++) {
                if (activeFile != null && currRoot.getChildren().get(i).getValue().equals(name)) {
                    TreeItem<String> removed = currRoot.getChildren().get(i);
                    removed.getParent().getChildren().remove(removed);
                    //
                    boolean b = toDelete.delete();
                    int j = currRoot.getChildren().size();
                    if (j == 0)
                        j++;
                    if (j > -1) {
                        activeFile = new File(activeDir.getAbsolutePath() + "\\\\" + currRoot.getChildren().get(j - 1).getValue());
                        activeFileName.setText("Current File: " + activeFile.getName());
                    } else {
                        activeFile = null;
                        activeFileName.setText("none");
                        codeArea.setText("");
                        codeArea.setVisible(true);
                    }
                }
            }
        }
    }

    @FXML
    void createFile(ActionEvent event) {
        CreateFile cf = new CreateFile();
        codeArea.setText("");
        codeArea.setVisible(true);
        cf.display("Create File", activeDir.getPath());
        activeFile = cf.getCreatedFile();
        activeFileName.setText("Current File: " + activeFile.getName());
        loadTree(activeDir, null);

    }

    @FXML
    void openFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File newFile;
        fc.setTitle("Open File");
        fc.setInitialDirectory(activeDir);
        newFile = fc.showOpenDialog(new Stage());
        currRoot.getChildren().add(new TreeItem<String>(newFile.toString()));
        tree.setRoot(currRoot);


    }

    @FXML
    void saveFile(ActionEvent event) {
        saveF();
    }

    @FXML
    void closeFile(ActionEvent event) {
        codeArea.clear();
        activeFileName.setText("");
        activeFile = null;
    }

    // Directory Fuctions
    @FXML
    void close(ActionEvent event) {
        codeArea.clear();
        tree.setRoot(null);
    }

    @FXML
    public void create(ActionEvent event) {
        Create c = new Create();
        c.display("Create");
        activeDir = c.getDirectory();
        loadTree(activeDir, null);
        //TODO: refresh no work
//        tree.setRoot(null);
//        loadTree(activeDir, null);
    }
    //Directory Functions
    //

    @FXML
    void open(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        activeDir = directoryChooser.showDialog(new Stage());
        loadTree(activeDir, null);
    }

    @FXML
    void save(ActionEvent event) {
        if (activeFile.exists() && activeFile != null) {
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
    }

    //Extra functions
    //
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

        if (parent == null) {
            tree.setRoot(root);
            currRoot = root;
        } else
            parent.getChildren().add(root);
        root.setExpanded(false);
    }

    // Saves any text changes to the current activeFile
    // Occurs when "save" is pressed or when selecting different file in tree view
    void saveF() {
        try {
            FileWriter fw = new FileWriter(activeFile.getAbsolutePath());
            fw.write(codeArea.getText());
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    String getNameOf(String s) {
        String out = "";
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.substring(i, i + 1).equals("\\"))
                index = i;
        }
        out = s.substring(index + 1, s.length());
        return out;
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'codeeditor.fxml'.";
        assert create != null : "fx:id=\"create\" was not injected: check your FXML file 'codeeditor.fxml'.";
        assert open != null : "fx:id=\"open\" was not injected: check your FXML file 'codeeditor.fxml'.";
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'codeeditor.fxml'.";
        assert activeFileName != null : "aFN not injected";

        // Selection, opens files
        codeArea.setVisible(false);
        tree.getSelectionModel().selectedItemProperty().addListener((observable, old_val, new_val) -> {
            StringBuilder pathBuilder = new StringBuilder();
            for (TreeItem<String> item = tree.getSelectionModel().getSelectedItem();
                 item != null; item = item.getParent()) {
                if (item.getParent() != null) {
                    pathBuilder.insert(0, item.getValue());
                    pathBuilder.insert(0, "/");
                }
            }
            codeArea.setVisible(true);
            if (activeFile != null) {
                saveF();
            }
            if (codeArea.getText() != "") {
                codeArea.clear();
            }
            //Reading text from selected file and inputing into codeArea
            String path = activeDir.getPath() + pathBuilder.toString();
            activeFile = new File(path);
            if (activeFile != null && !activeFile.isDirectory()) {
                javafx.application.Platform.runLater(() -> {
                    activeFileName.setText("Current File: " + activeFile.getName());
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(activeFile));
                        String s;
                        while ((s = bufferedReader.readLine()) != null) {
                            codeArea.appendText(s);
                            codeArea.appendText("\n");
                        }
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
                //Not needed: codeArea.setStyle("-fx-font-family: Times New Roman");
            }
        });
        // COLOR CODING not working: Idea is to read in the text from the codeArea
        //
             codeArea.textProperty().addListener(new ChangeListener <String>() {
        	//@Override
			public void changed(ObservableValue <? extends String> arg0, String arg1, String arg2) {
				String old=codeArea.getText();
				StringBuilder z = new StringBuilder();
				for(int i=0;i<old.length();i++) {
					//Text t =new Text();
					if(i+1<old.length() && old.substring(i,i+1)=="if") {
						z.append("if").append("<span style=\"color:Blue\">");
						codeArea.appendText(z.toString());
					}
                    else if(i+1<old.length() && old.substring(i,i+1)=="else") {
                        z.append("else").append("<span style=\"color:Blue\">");
                        codeArea.appendText(z.toString());
                    }
                    else if(i+1<old.length() && old.substring(i,i+1)=="for") {
                        z.append("for").append("<span style=\"color:Blue\">");
                        codeArea.appendText(z.toString());
                    }
                    else if(i+1<old.length() && old.substring(i,i+1)=="while") {
                        z.append("while").append("<span style=\"color:Blue\">");
                        codeArea.appendText(z.toString());
                    }
                    else if(i+1<old.length() && old.substring(i,i+1)=="+") {
                        z.append("+").append("<span style=\"color:Red\">");
                        codeArea.appendText(z.toString());
                    }
                    else if(i+1<old.length() && old.substring(i,i+1)=="-") {
                        z.append("-").append("<span style=\"color:Red\">");
                        codeArea.appendText(z.toString());
                    }
                    else if(i+1<old.length() && old.substring(i,i+1)=="/") {
                        z.append("/").append("<span style=\"color:Red\">");
                        codeArea.appendText(z.toString());
                    }
                    else if(i+1<old.length() && old.substring(i,i+1)=="&&") {
                        z.append("&&").append("<span style=\"color:Red\">");
                        codeArea.appendText(z.toString());
                    }
                    else if(i+1<old.length() && old.substring(i,i+1)=="||") {
                        z.append("||").append("<span style=\"color:Red\">");
                        codeArea.appendText(z.toString());
                    }
                    codeArea.appendText(old.substring(i,i+1));
				}

        		//codeArea.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick; -fx-font-size: 10px;");
        		//codeArea.setStyle("-fx-text-fill: blue ;") ;
        	}
        };
    }
}
