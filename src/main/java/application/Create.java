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

import java.awt.*;
import java.util.ArrayList;


public class Create {
	
	public static TextField textField=new TextField();
	public static ArrayList<Project>projects= new ArrayList<Project>();

	public static void display(String title) {
		Stage window= new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinHeight(250);
		window.setMinWidth(250);
		Button enter= new Button("Enter");
		enter.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				Project project=new Project();
				project.setName(getTextField());
				project.print();
				projects.add(project);
				textField.clear();
				window.close();
			}
		});
		
		VBox layout=new VBox(10);
		layout.getChildren().addAll(textField,enter);
		layout.setAlignment(Pos.TOP_LEFT);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
	}
	public static String getTextField() {
		return(textField.getText());
	}
}
