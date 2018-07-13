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
		stage = primaryStage;
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
}
