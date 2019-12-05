package application;
import bsh.EvalError;
import groovy.lang.GroovyShell;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.codehaus.groovy.*;
import bsh.Interpreter;

import javax.naming.Binding;
import javax.tools.*;
import java.io.*;
import java.util.ArrayList;

public class Compile {
    private String sourceCodePath;
    private String contents;

    public Compile(String s,String c){
        sourceCodePath=s;
        contents=c;
    }
    public void compile() throws IOException, EvalError {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Output");
        window.setMinHeight(250);
        window.setMinWidth(250);
        Text output=new Text();
        VBox layout=new VBox();
        layout.getChildren().addAll(output);
        JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
        int result=compiler.run(null,null,null,sourceCodePath);
        if(result==0){
            output.setText("Successful compile.");
        }
        else{
            output.setText("Unsuccessful compile.");
        }
        //execute code
      






        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
