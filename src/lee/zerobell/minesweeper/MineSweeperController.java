package lee.zerobell.minesweeper;

import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lee.zerobell.minesweeper.model.*;

public class MineSweeperController {

	private MineModel model;
	private Thread timerThread;
	private Main main;
	
	private String level;
	
	public MineSweeperController() {
		this.model = new MineModel();
		model.setController(this);
	}
	
	public void setMainApp(Main main) {
		this.main = main;
		init();
	}
	
	public void init() {
		Util.setImages();
		Tile.setModel(model);
		Tile.setController(this);
		model.setMode(Util.MODE_EASY);
		setMinePaneSize();
		getReady();
		resetBtn.setOnAction(e->restart());
		start_easy();
	}
	
	@FXML
	private GridPane minePane;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private Label mineLabel;
	
	@FXML
	private Button resetBtn;
	
	@FXML
	private StackPane mineLayout;
	
	@FXML
	private HBox controlLayout;
	
	@FXML
	public void start_easy(Event e) {
		this.level = "easy";
		
		main.setWindowSize(310, 410);
		model.setMode(Util.MODE_EASY);
		controlLayout.setPrefHeight(100);
		mineLayout.setPrefWidth(Util.TILE_WIDTH * Util.MODE_EASY[0]);
		mineLayout.setPrefHeight(Util.TILE_HEIGHT * Util.MODE_EASY[1]);
		minePane.getChildren().clear();
		setMinePaneSize();
		getReady();
	}
	
	public void start_easy() {
		this.level = "easy";
		
		main.setWindowSize(310, 410);
		model.setMode(Util.MODE_EASY);
		controlLayout.setPrefHeight(100);
		mineLayout.setPrefWidth(Util.TILE_WIDTH * Util.MODE_EASY[0]);
		mineLayout.setPrefHeight(Util.TILE_HEIGHT * Util.MODE_EASY[1]);
		minePane.getChildren().clear();
		setMinePaneSize();
		getReady();
	}
	
	@FXML
	public void start_normal(Event e) {
		this.level = "normal";
		
		main.setWindowSize(600, 600);
		mineLayout.setPrefWidth(Util.TILE_WIDTH * Util.MODE_NORMAL[0]);
		mineLayout.setPrefHeight(Util.TILE_HEIGHT * Util.MODE_NORMAL[1]);
		controlLayout.setPrefHeight(80);
		model.setMode(Util.MODE_NORMAL);
		minePane.getChildren().clear();
		setMinePaneSize();
		getReady();
	}
	
	@FXML
	public void start_hard(Event e) {
		this.level = "hard";
		
		main.setWindowSize(800, 800);
		mineLayout.setPrefWidth(Util.TILE_WIDTH * Util.MODE_HARD[0]);
		mineLayout.setPrefHeight(Util.TILE_HEIGHT * Util.MODE_HARD[1]);
		controlLayout.setPrefHeight(100);
		model.setMode(Util.MODE_HARD);
		minePane.getChildren().clear();
		setMinePaneSize();
		getReady();
	}
	
	@FXML
	public void start_custom(Event e) {
		AnchorPane customPane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/CustomLayout.fxml"));
			
			customPane = (AnchorPane) loader.load();
			CustomController customController = loader.getController();
			

			Scene customScene = new Scene(customPane);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Custom Game");
			dialogStage.initOwner(main.getStage());
			dialogStage.setScene(customScene);
			
			customController.setDialogStage(dialogStage);
			customController.init();
			customController.setMCController(this);
			
			dialogStage.showAndWait();;
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	
	public void set_custom(int cols, int rows, int mines) {
		this.level = "custom";
		
		calcSize(new int[]{ cols, rows });
		model.setMode(new int[] {cols, rows, mines});
		minePane.getChildren().clear();
		setMinePaneSize();
		getReady();
	}
	
	public void calcSize(int[] setting) {
		int paneWidth = Util.TILE_WIDTH * setting[0];
		int paneHeight = Util.TILE_HEIGHT * setting[1];
		
		main.setWindowSize(paneWidth + 100, paneHeight + 200);
		mineLayout.setPrefSize(paneWidth, paneHeight);
		controlLayout.setPrefHeight(100);
	}
	
	@FXML
	public void show_about(Event e) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("MineSweeper Ver 0.12");
		alert.setContentText("Programmed by Zerobell Lee.");

		alert.showAndWait();
	}
	
	@FXML
	public void viewRecord(Event e) {
		main.showRecord();
	}
	
	public void startTimer() {

		timerThread = new Thread(new Timer());
		timerThread.start();
	}
	
	public void restart() {
		minePane.getChildren().clear();
		setMinePaneSize();
		getReady();
	}
	
	private class Timer implements Runnable {
		@Override
		public void run() {
			while(model.getState()==(Util.STARTED)) {
				try {
					model.addTimer();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							timeLabel.setText(Integer.toString(model.getTimer()));
						}
					});
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setMinePaneSize() {
		int cols = model.getCols();
		int rows = model.getRows();
		minePane.setPrefHeight(Util.TILE_HEIGHT*rows);
		minePane.setPrefWidth(Util.TILE_WIDTH*cols);
		minePane.setMaxHeight(Util.TILE_HEIGHT*rows);
		minePane.setMaxWidth(Util.TILE_WIDTH*cols);
	}
	
	public void gameOver() {
		gameOver(false);
	}
	
	public void gameOver(boolean victory) {
		model.setState(Util.GAMEOVER);
		if (victory) {
			Preferences prefs = Preferences.userNodeForPackage(Main.class);
			resetBtn.getStyleClass().clear();
			resetBtn.getStyleClass().add("gameend");
			
			int thisTime = Integer.parseInt(timeLabel.getText());
			int recordTime;
			
			TextInputDialog dialog = new TextInputDialog("zerobell");
			dialog.setTitle("Write your name");
			dialog.setHeaderText("Congratulations! You've broken the record.");
			dialog.setContentText("Input your name");
			
			if (this.level == "easy") {
				recordTime = Integer.parseInt(prefs.get("easy_record", "999"));
				if (thisTime<recordTime) {
					Optional<String> newName = dialog.showAndWait();
					if (newName.isPresent()) {
						prefs.put("easy_name", newName.get());
					}
					else {
						prefs.put("easy_name", "zerobell");
					}
					prefs.put("easy_record", String.valueOf(thisTime));
				}
			}
			else if (this.level == "normal") {
				recordTime = Integer.parseInt(prefs.get("normal_record", "999"));
				if (thisTime<recordTime) {
					Optional<String> newName = dialog.showAndWait();
					if (newName.isPresent()) {
						prefs.put("normal_name", newName.get());
					}
					else {
						prefs.put("normal_name", "zerobell");
					}
					prefs.put("normal_record", String.valueOf(thisTime));
				}
			}
			else if (this.level == "hard") {
				recordTime = Integer.parseInt(prefs.get("hard_record", "999"));
				if (thisTime<recordTime) {
					Optional<String> newName = dialog.showAndWait();
					if (newName.isPresent()) {
						prefs.put("hard_name", newName.get());
					}
					else {
						prefs.put("hard_name", "zerobell");
					}
					prefs.put("hard_record", String.valueOf(thisTime));
				}
			}
		}
		else {
			resetBtn.getStyleClass().clear();
			resetBtn.getStyleClass().add("gameover");
		}
	}
	
	public void getReady() {
		int cols = model.getCols();
		int rows = model.getRows();
		
		model.init();
		Tile[][] tileArr = model.getTiles();
		
		resetBtn.getStyleClass().clear();
		resetBtn.getStyleClass().add("gameready");
		//resetBtn.setGraphic(Util.GameReadyBitmap);
		
		for (int i = 0; i<cols; i++) {
			for (int j = 0; j<rows; j++) {
				Tile tile = tileArr[i][j];
				tile.addEventFilter(MouseEvent.MOUSE_PRESSED, e->handleTileMousePressed(e));
				tile.addEventFilter(MouseEvent.MOUSE_RELEASED, e->handleTileMouseReleased(e));
				
				minePane.add(tile, i, j);
			}
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mineLabel.setText(Integer.toString(model.getMines()));
				timeLabel.setText("0");
			}
		});
	
	}
	
	public void handleTileMousePressed(MouseEvent e) {
		Tile tile = (Tile) e.getSource();
		if (e.getButton() == MouseButton.PRIMARY) {
			if (tile.getClickedState()==Util.SECONDARY_CLICKED)
				tile.setClickedState(Util.DUAL_CLICKED);
			else
				tile.setClickedState(Util.PRIMARY_CLICKED);
		}
		else if (e.getButton() == MouseButton.SECONDARY) {
			if (tile.getClickedState()==Util.PRIMARY_CLICKED)
				tile.setClickedState(Util.DUAL_CLICKED);
			else
				tile.setClickedState(Util.SECONDARY_CLICKED);
		}
	}
	
	public void handleTileMouseReleased(MouseEvent e) {
		Tile tile = (Tile) e.getSource();
		if (tile.getClickedState() == Util.DUAL_CLICKED) {
			tile.open(true);
		}
		else if (tile.getClickedState() == Util.PRIMARY_CLICKED) {
			tile.open(false);
		}
		else
			tile.mark();
		tile.setClickedState(Util.NOT_CLICKED);
	}
	
	public void subtractMine() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mineLabel.setText(Integer.toString((Integer.parseInt(mineLabel.getText()) - 1)));
			}
		});
	}
	
	public void addMine() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mineLabel.setText(Integer.toString((Integer.parseInt(mineLabel.getText()) + 1)));
			}
		});
	}
}
