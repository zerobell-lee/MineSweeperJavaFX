package lee.zerobell.minesweeper;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {

	private AnchorPane mainPane;
	private Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MainLayout.fxml"));
			
			mainPane = (AnchorPane) loader.load();
			MineSweeperController mineSweeperController = loader.getController();
			mineSweeperController.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public void setWindowSize(int width, int height) {
		this.stage.setWidth(width);
		this.stage.setHeight(height);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void setSize(int width, int height) {
		
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void showRecord() {
		AnchorPane recordPane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RecordLayout.fxml"));
			
			recordPane = (AnchorPane) loader.load();
			ViewRecordController viewRecordController = loader.getController();
			

			Scene recordScene = new Scene(recordPane);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Record");
			dialogStage.initOwner(stage);
			dialogStage.setScene(recordScene);
			
			viewRecordController.setDialogStage(dialogStage);
			viewRecordController.init();
			
			dialogStage.showAndWait();;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
