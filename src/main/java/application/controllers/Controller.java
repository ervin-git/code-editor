package application.controllers;


import application.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller implements Initializable {

    private static final String[] KEYWORDS = new String[]{
            "if", "else", "for", "while"
    };
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String ARITHMETIC_PATTERN = "([/+\\-*]|[|&^!]|[=]+[=]|[!]|[=])";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<ARITHMETIC>" + ARITHMETIC_PATTERN + ")"
    );

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
    private MenuItem compile;

    // Execute
    @FXML
    private MenuItem execute;

    // Statistics
    @FXML
    private MenuItem stats;

    // Labels
    @FXML
    private Label activeFileName;

    // Code
    @FXML
    private CodeArea codeArea;
    private ExecutorService executorService;

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
        System.out.println("project open");
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
        }
    */
    }

    // File Functions
    @FXML
    void file_create(ActionEvent event) {
        project.createFile();
        codeArea.clear();
        codeArea.setVisible(true);
        activeFileName.setText("Current File: " + project.getActiveFile().getName());

        loadTree(project.getDirectory(), null);

        // FIXME: Selection to the newly created file doesnt work
        MultipleSelectionModel<TreeItem<String>> multipleSelectionModel = tree.getSelectionModel();
        for (int i = 0; i < currRoot.getChildren().size(); i++) {
            if (currRoot.getChildren().get(i).getValue().equals(project.getActiveFile().getName()))
                multipleSelectionModel.select(currRoot.getChildren().get(i));
        }
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
        if (codeArea.isVisible() && codeArea.getLength() == 0)
            codeArea.clear();
        if (!file.isDirectory()) {
            javafx.application.Platform.runLater(() -> {
                activeFileName.setText("Current File: " + file.getName());
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String s;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((s = bufferedReader.readLine()) != null) {
                        stringBuilder.append(s);
                        stringBuilder.append("\n");
                    }
                    bufferedReader.close();
                    codeArea.replaceText(0, 0, stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
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
    }

    // Compile
    @FXML
    void compile(ActionEvent event) throws IOException {
        Compile compile = new Compile(project, project.getActiveFile());
        List<String> comp = compile.compile();
        for (String str : comp) {
            project.addClass(str);
//            project.addClass(str.substring(str.lastIndexOf("/") + 1, str.length()));
        }
    }

    // Execute
    @FXML
    void execute(ActionEvent event) {
        Execute execute = new Execute(project);
        execute.execute();
//        CompilingClassLoader loader = new CompilingClassLoader();
//        project.getFiles().forEach(f -> {
//            try {
//                Class<?> c = loader.loadClass(f.getName());
//                Object ob = c.getDeclaredConstructor().newInstance();
//                Method main = c.getMethod("main");
//                main.invoke(ob);
//            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        });
    }

    // Stats
    @FXML
    void stats(ActionEvent event) {
        Stats stats = new Stats(codeArea.getText());
        stats.displayStats();
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

    // Gets all the highlighting details from the java-keywords.css file
    // Uses regex to match keywords/arithmetic functions and strings
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("ARITHMETIC") != null ? "arithmetic" :
                                    matcher.group("STRING") != null ? "string" :
                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    // Computes highlighting with async
    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executorService.execute(task);
        return task;
    }

    // Applys highlighting
    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        executorService = Executors.newSingleThreadExecutor();

        // async project loader, used fxmisc docs as a resource
        // using sync to prevent duplicates
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        Subscription cleanupWhenDone = codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(250))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeArea.multiPlainChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
        codeArea.setVisible(false);

        // chooses selection from tree view
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
            if (project.getActiveFile() != null)
                saveF();

            codeArea.clear();
            //Reading text from selected file and inputting into codeArea
            String path = project.getDirectory().getPath() + pathBuilder.toString();
            project.setActiveFile(new File(path));
            if (!project.getActiveFile().isDirectory()) {
                javafx.application.Platform.runLater(() -> {
                    activeFileName.setText("Current File: " + project.getActiveFile().getName());
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(project.getActiveFile()));
                        String s;
                        while ((s = bufferedReader.readLine()) != null) {
                            stringBuilder.append(s);
                            stringBuilder.append("\n");
                        }
                        codeArea.replaceText(0, 0, stringBuilder.toString());
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
    }
}
