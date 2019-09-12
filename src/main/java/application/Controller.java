package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
	@FXML
	public Label label;
	public MenuItem menuItem;
	
	public void initialize() {
		
	}
	@FXML
	private void changeText(ActionEvent event) {
		menuItem.setText("Change Text");
	}
}
