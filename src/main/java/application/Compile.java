package application;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.tools.*;
import java.io.File;
import java.io.IOException;

public class Compile {
    private Project project;
    private File file;

    public Compile(Project project, File file) {
        this.project = project;
        this.file = file;
    }

    public void compile() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Output");
        window.setMinHeight(250);
        window.setMinWidth(250);
        TextArea textArea = new TextArea();
        VBox layout = new VBox();
        layout.getChildren().addAll(textArea);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                diagnostics, null, null);

        // Compile the file
        boolean success = compiler.getTask(null, fileManager, null, null, null,
                fileManager.getJavaFileObjects(file)).call();
        fileManager.close();

        if (success) {
            textArea.appendText("Successful compile.\n");
        } else {
            textArea.appendText("Unsuccessful compile.\n");
        }
        textArea.appendText(diagnostics.getDiagnostics().toString());

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
