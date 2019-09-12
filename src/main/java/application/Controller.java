package application;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;


import java.awt.List;
import javafx.stage.FileChooser;

import java.beans.EventHandler;
import java.io.*;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;









public class Controller {
	
	ArrayList<Project>projects= new ArrayList<Project>();

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
    private TreeView tree;

    @FXML
    void close(ActionEvent event) {
    }

    @FXML
    public void create(ActionEvent event) {
    	create.setOnAction(e ->Create.display("Create"));
    }

    @FXML
    void open(ActionEvent event) throws IOException {
    	DirectoryChooser directoryChooser=new DirectoryChooser();
    	File selectedDirectory = directoryChooser.showDialog(Main.getStage());
    	String fileName=selectedDirectory.getName();
    	String location=selectedDirectory.getAbsolutePath();
    	//

    	TreeItem<String> rootItem = new TreeItem<String> (fileName);
        rootItem.setExpanded(true);
        tree.setRoot(rootItem);
        tree = new TreeView<String> (rootItem);    
        System.out.println(tree.getRoot());		
         
         
   
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
        
   


    }

}

