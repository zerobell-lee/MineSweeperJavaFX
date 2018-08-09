package lee.zerobell.minesweeper;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import lee.zerobell.minesweeper.model.Util;

public class CustomController {
	
	private Stage dialogStage;
	private MineSweeperController mc;
	
	@FXML
	TextField rowText;
	
	@FXML
	TextField colText;
	
	@FXML
	TextField mineText;
	
	@FXML
	Label rowLabel;
	
	@FXML
	Label colLabel;
	
	@FXML
	Label mineLabel;
	
	@FXML
	public void createGame(Event e) {
		int rowVal = Integer.parseInt(rowText.getText());
		int colVal = Integer.parseInt(colText.getText());
		int mineVal = Integer.parseInt(mineText.getText());
		
		StringBuilder errorSb = new StringBuilder();
		
		if (rowVal<Util.MIN_ROW)
			errorSb.append("* Rows should be more than " + Util.MIN_ROW + "\n");
		if (rowVal>Util.MAX_ROW)
			errorSb.append("* Rows should be less than " + Util.MAX_ROW + "\n");
		if (colVal<Util.MIN_COL)
			errorSb.append("* Cols should be more than " + Util.MIN_COL + "\n");
		if (colVal>Util.MAX_COL)
			errorSb.append("* Cols should be less than " + Util.MAX_COL + "\n");
		if (mineVal<Util.MIN_MINE)
			errorSb.append("* Mines should be more than " + Util.MIN_MINE + "\n");
		if (mineVal>Util.MAX_MINE)
			errorSb.append("* Mines should be less than " + Util.MAX_MINE + "\n");
		
		if (mineVal>rowVal*colVal)
			errorSb.append("* Mines are too much. Mines should less than " + rowVal*colVal);
		
		if (errorSb.length()>0) {
			//Showing error msg
			Alert alert =new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Can't make a game. Check the following.");
			alert.setContentText(errorSb.toString());
			alert.showAndWait();
		}
		else {
			//Make games
			mc.set_custom(colVal, rowVal, mineVal);
			dialogStage.close();
		}
		
		
	}
	
	public void init() {
		rowLabel.setText(String.format("Rows(%d-%d)", Util.MIN_ROW, Util.MAX_ROW));
		colLabel.setText(String.format("Cols(%d-%d)", Util.MIN_COL, Util.MAX_COL));
		mineLabel.setText(String.format("Mines(%d-%d)", Util.MIN_MINE, Util.MAX_MINE));
	}
	
	public void setMCController(MineSweeperController mc) {
		this.mc = mc;
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
