package lee.zerobell.minesweeper;

import java.util.Optional;
import java.util.prefs.Preferences;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ViewRecordController {
	
	private Stage dialogStage;
	
	@FXML
	Label easy_name;
	
	@FXML
	Label easy_record;
	
	@FXML
	Label normal_name;
	
	@FXML
	Label normal_record;
	
	@FXML
	Label hard_name;
	
	@FXML 
	Label hard_record;
	
	@FXML
	Button resetBtn;
	
	public void init() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		easy_name.setText(prefs.get("easy_name", "zerobell"));
		easy_record.setText(prefs.get("easy_record", "999"));
		
		normal_name.setText(prefs.get("normal_name", "zerobell"));
		normal_record.setText(prefs.get("normal_record", "999"));
		
		hard_name.setText(prefs.get("hard_name", "zerobell"));
		hard_record.setText(prefs.get("hard_record", "999"));
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	@FXML
	public void reset(Event e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Reset");
		alert.setHeaderText("This will reset all records");
		alert.setContentText("Do you want to proceed?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Preferences prefs = Preferences.userNodeForPackage(Main.class);
			prefs.put("easy_name", "zerobell");
			prefs.put("normal_name", "zerobell");
			prefs.put("hard_name", "zerobell");
			
			prefs.put("easy_record", "999");
			prefs.put("normal_record", "999");
			prefs.put("hard_record", "999");
			init();
		}
	}
	
}
