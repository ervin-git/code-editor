package application;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Execute {
    private Project project;

    public Execute(Project project) {
        this.project = project;
    }

    public void execute() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Output");
        window.setMinHeight(250);
        window.setMinWidth(250);
        TextArea textArea = new TextArea();
        VBox layout = new VBox();
        layout.getChildren().addAll(textArea);
        // Execute logic

        // make jar
        StringBuilder cmd = new StringBuilder("jar cf jar.jar ");
        for (String str : project.getClasses()) {
            cmd.append(str);
        }
        System.out.println(cmd.toString());
        try {
            runProcess(cmd.toString());
        } catch (Exception e) {
            textArea.appendText(e.toString());
        }

        // run jar
        StringBuilder cmd2 = new StringBuilder("java -verbose jar.jar");
        try {
            runProcess(cmd.toString());
        } catch (Exception e) {
            textArea.appendText(e.toString());
        }

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }
}
