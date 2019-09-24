package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Project {

    private String name;
    private File directory;
    private List<File> files = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();
    //TODO: lib folder, dependencies
    //      open/create/remove/close/save file
    //      reorganize structure of the project

    public Project(String name, File directory) {
        this.name = name;
        this.directory = directory;
        files.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
    }

    // Getter
    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public List<File> getFiles() {
        return files;
    }

    // Other
    boolean contains(File file) {
        return files.contains(file);
    }

    boolean addFile(File file) {
        if (!files.contains(file)) {
            files.add(file);
            return true;
        } else
            return false;
    }

    boolean removeFile(File file) {
        if (files.contains(file)) {
            files.remove(file);
            return true;
        } else
            return false;
    }

    File getFile(String fileName) {
        return files.stream().filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
    }

    void refreshList() {
        files.clear();
        files.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
    }
}
