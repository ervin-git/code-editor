package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Project {

    private String name;
    private File directory, activeFile;
    private List<File> files = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();

    public Project(String name, File directory) {
        this.name = name;
        this.directory = directory;
        files.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
    }

    // Getter
    public File getActiveFile() {
        return activeFile;
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public List<File> getFiles() {
        return files;
    }

    // Setter
    public void setActiveFile(File activeFile) {
        this.activeFile = activeFile;
    }

    // Other
    public boolean contains(File file) {
        return files.contains(file);
    }

    public void addDependency(String dep) {
        dependencies.add(dep);
    }

    public void removeDependency(String dep) {
        dependencies.remove(dep);
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(File file) {
        files.remove(file);
    }

    public File getFile(String fileName) {
        return files.stream().filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
    }

    public void refreshList() {
        files.clear();
        files.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
    }

    // Functionality
    public void createFile() {
        Stage createWindow = new Stage();
        TextField textField = new TextField();

        createWindow.initModality(Modality.APPLICATION_MODAL);
        createWindow.setTitle("Create File");
        createWindow.setMinHeight(250);
        createWindow.setMinWidth(250);
        Button enter = new Button("Enter");
        enter.setOnAction(event -> {
            if (textField.getText() != null) {
                File newF = new File(directory + File.separator + textField.getText());
                try {
                    newF.createNewFile();
                } catch (IOException e) {
                    System.out.println(e);
                }
                textField.clear();
                createWindow.close();
                files.add(newF);
                activeFile = newF;
            }
        });
        Label label = new Label("File Name");
        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, textField, enter);
        layout.setAlignment(Pos.TOP_LEFT);

        Scene scene = new Scene(layout);
        createWindow.setScene(scene);
        createWindow.showAndWait();
    }

    public boolean deleteFile(File file) {
        if (files.contains(file)) {
            files.remove(file);
            return file.delete();
        } else {
            return false;
        }
    }
}
