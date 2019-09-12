package application;

import java.awt.List;
import java.beans.EventHandler;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;


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
    void close(ActionEvent event) {
    }

    @FXML
    public void create(ActionEvent event) {
    	create.setOnAction(e ->Create.display("Create"));
 
    	
    }

    @FXML
    void open(ActionEvent event) {
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

