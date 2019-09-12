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
    void close(ActionEvent event) {
    }

    @FXML
    public void create(ActionEvent event) {
    	create.setOnAction(e ->Create.display("Create"));
    }

    @FXML
    void open(ActionEvent event) throws IOException {
    	FileChooser directoryChooser=new FileChooser();
    	File selectedDirectory = directoryChooser.showOpenDialog(Main.getStage());
    	if(selectedDirectory != null){
    		try {
    		        Scanner s = new Scanner(selectedDirectory);
    		        while (s.hasNextLine()) {
    		        	System.out.println(s.nextLine());
    		        	codeArea.appendText(s.nextLine()); }
    		        }
    			catch (FileNotFoundException ex) {
    		        System.err.println(ex);
    		    }
    			catch(NullPointerException ex) {
    				System.err.println(ex);
    			}
    			
    	}
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

