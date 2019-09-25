package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.*;
import javafx.scene.canvas.*; 
import javafx.scene.web.*; 
import javafx.scene.Group; 

import java.awt.*;
import javafx.scene.control.Label;
import java.util.ArrayList;


public class Create {
	
	private static TextField textField=new TextField();
	private static TextField textField2=new TextField();
	private static File dirName;

	public static void display(String title) {
		Stage window= new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinHeight(250);
		window.setMinWidth(250);
		Button enter= new Button("Enter");
		enter.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				if(textField.getText()!=null && textField2.getText()!=null){
					StringBuffer sBuffer = new StringBuffer();
					sBuffer.append(textField2.getText()).append("\\\\").append(textField.getText());
					textField.clear();
					textField2.clear();
					File file=new File(sBuffer.toString());
					if(!file.exists()){
						dirName=file;
						boolean result = file.mkdir();
					}
				
					window.close();
				}
			}
		});
		Button lookup=new Button("Lookup");
		lookup.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser=new DirectoryChooser();
		    	File selectedDirectory = directoryChooser.showDialog(Main.getStage());
		    	textField2.setText(selectedDirectory.getAbsoluteFile().toString());
			}
		});
		Label label=new Label("Project Name");
		Label label2=new Label("Location");
		VBox layout=new VBox(15);	
		layout.getChildren().addAll(label,textField,label2,textField2,lookup,enter);
		layout.setAlignment(Pos.TOP_LEFT);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
	}
	public static String getTextField() {
		return(textField.getText());
	}
	public static File getDirectory() {
		return(dirName);
	}
}
