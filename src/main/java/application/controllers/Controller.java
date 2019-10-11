package application.controllers;


import application.Create;
import application.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.lang.*;

public class Controller implements Initializable {

    private Project project;

    // Menus
    // Project
    @FXML
    private MenuItem project_open;
    @FXML
    private MenuItem project_create;
    @FXML
    private MenuItem project_close;
    @FXML
    private MenuItem project_save;
    // File
    @FXML
    private MenuItem file_create;
    @FXML
    private MenuItem file_open;
    @FXML
    private MenuItem file_close;
    @FXML
    private MenuItem file_save;
    @FXML
    private MenuItem file_remove;
    @FXML
    private MenuItem file_pref;
    @FXML
    private MenuItem file_quit;
    // Edit
    // Compile
    @FXML
    private Menu compile;
    // Execute
    @FXML
    private Menu execute;
    // Statistics
    @FXML
    private Menu stats;

    // Labels
    @FXML
    private Label activeFileName;

    // Code
    @FXML
    private TextArea codeArea;

    // Tree
    private TreeItem<String> currRoot;
    @FXML
    private TreeView<String> tree;


    // Other
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;


    // Project Functions
    @FXML
    void project_open(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(new Stage());
        project = new Project(dir.getName(), dir);
        loadTree(project.getDirectory(), null);
    }

    @FXML
    void project_create(ActionEvent event) {
        if (project != null) project = null; // i feel like this is bad to do
        Create create = new Create();
        project = new Project(create.getDirectory().getName(), create.getDirectory());
        loadTree(project.getDirectory(), null);
        Path lib = Paths.get(project.getDirectory().getPath() + File.separator + "libs");
        if (Files.notExists(lib))
            new File(String.valueOf(lib)).mkdirs();
    }

    @FXML
    void project_close(ActionEvent event) {
        project = null;
        codeArea.clear();
        activeFileName.setText("");
        codeArea.setVisible(false);
        tree.setRoot(null);
    }

    @FXML
    void project_save(ActionEvent event) {
        //TODO: figure out what this does
/*  This is saving a active file, that would be handled in the file save, maybe this can save all the files but we already auto do that, so idk
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
        }*/
    }

    // File Functions
    @FXML
    void file_create(ActionEvent event) {
        project.createFile();
        codeArea.setText("");
        codeArea.setVisible(true);
        activeFileName.setText("Current File: " + project.getActiveFile().getName());

        loadTree(project.getDirectory(), null);

        // FIXME: Selection to the newly created file doesnt work
        MultipleSelectionModel<TreeItem<String>> multipleSelectionModel = tree.getSelectionModel();
        for (int i = 0; i < currRoot.getChildren().size(); i++) {
            if (currRoot.getChildren().get(i).getValue().equals(project.getActiveFile().getName()))
                multipleSelectionModel.select(currRoot.getChildren().get(i));
        }
        /*
        CreateFile cf = new CreateFile();
        codeArea.setText("");
        codeArea.setVisible(true);
        cf.display("Create File", project.getActiveFile().getPath());
        activeFile = cf.getCreatedFile();
        activeFileName.setText("Current File: " + activeFile.getName());
        loadTree(project.getDirectory(), null);
        */
    }

    @FXML
    void file_open(ActionEvent event) {
        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(project.getDirectory());
        file = fileChooser.showOpenDialog(new Stage());

        MultipleSelectionModel<TreeItem<String>> multipleSelectionModel = tree.getSelectionModel();
        for (int i = 0; i < currRoot.getChildren().size(); i++) {
            if (currRoot.getChildren().get(i).getValue().equals(file.getName()))
                multipleSelectionModel.select(currRoot.getChildren().get(i));
        }

        activeFileName.setText("");
        if (codeArea.isVisible() && !codeArea.getText().equals(""))
            codeArea.clear();
        if (!file.isDirectory()) {
            javafx.application.Platform.runLater(() -> {
                activeFileName.setText("Current File: " + file.getName());
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String s;
                    while ((s = bufferedReader.readLine()) != null) {
//                        codeArea.getChildren().add(new Text(s));
                        codeArea.appendText(s);
                        codeArea.appendText("\n");
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
/*        FileChooser fc = new FileChooser();
        File newFile;
        fc.setTitle("Open File");
        fc.setInitialDirectory(activeDir);
        newFile = fc.showOpenDialog(new Stage());
        currRoot.getChildren().add(new TreeItem<String>(newFile.toString()));
        tree.setRoot(currRoot);*/
    }

    @FXML
    void file_close(ActionEvent event) {
        codeArea.clear();
        codeArea.setVisible(false);
        activeFileName.setText("");
        project.setActiveFile(null);
    }

    @FXML
    void file_save(ActionEvent event) {
        saveF();
    }

    @FXML
    void file_remove(ActionEvent event) {
        if (project.getActiveFile() != null) {
            codeArea.clear();
            activeFileName.setText("");
            project.deleteFile(project.getActiveFile());
            loadTree(project.getDirectory(), null);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No active file to remove", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
//        //Find the current File from the TreeView then remove it from the tree view
//        if (activeFile != null) {
//            String name = getNameOf(activeFile.toString());
//            File toDelete = activeFile;
//            for (int i = 0; i < currRoot.getChildren().size(); i++) {
//                if (activeFile != null && currRoot.getChildren().get(i).getValue().equals(name)) {
//                    TreeItem<String> removed = currRoot.getChildren().get(i);
//                    removed.getParent().getChildren().remove(removed);
//                    //
//                    boolean b = toDelete.delete();
//                    int j = currRoot.getChildren().size();
//                    if (j == 0)
//                        j++;
//                    if (j > -1) {
//                        activeFile = new File(activeDir.getAbsolutePath() + "\\\\" + currRoot.getChildren().get(j - 1).getValue());
//                        activeFileName.setText("Current File: " + activeFile.getName());
//                    } else {
//                        activeFile = null;
//                        activeFileName.setText("none");
//                        codeArea.setText("");
//                        codeArea.setVisible(true);
//                    }
//                }
//            }
//        }
    }

    // Compile
    @FXML
    void compile(ActionEvent event) {
        Compiler.enable();
        String ProjectName = project.getName();
        String ProjectCode = codeArea.getText();
        Class CompiledProjectName = ProjectCode.getClass();

        Compiler.disable();
    }
    private static class CompilerClass
    {
        public CompilerClass()
        {
        }
    }

    // Execute
    @FXML
    void execute(ActionEvent event) {

    }

    // Stats
    @FXML
    void stats(ActionEvent event) {

    }

    //Extra functions
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
    }

    // Saves any text changes to the current activeFile
    // Occurs when "save" is pressed or when selecting different file in tree view
    private void saveF() {
        try {
            FileWriter fw = new FileWriter(project.getActiveFile().getAbsolutePath());
            fw.write(codeArea.getText());
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

/*    String getNameOf(String s) {
        String out = "";
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.substring(i, i + 1).equals("\\"))
                index = i;
        }
        out = s.substring(index + 1, s.length());
        return out;
    }*/

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            if (project.getActiveFile() != null) {
                saveF();
            }
            if (!codeArea.getText().equals("")) {
                codeArea.clear();
            }
            //Reading text from selected file and inputing into codeArea
            String path = project.getDirectory().getPath() + pathBuilder.toString();
            project.setActiveFile(new File(path));
            if (!project.getActiveFile().isDirectory()) {
                javafx.application.Platform.runLater(() -> {
                    activeFileName.setText("Current File: " + project.getActiveFile().getName());
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(project.getActiveFile()));
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
            } else {
                codeArea.setVisible(false);
                activeFileName.setText("");
            }
        });
        // COLOR CODING not working: Idea is to read in the text from the codeArea
        /*
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
        });*/
    }
}
