package lee.zerobell.minesweeper;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lee.zerobell.minesweeper.model.*;

public class MineSweeperController {

	private MineModel model;
	private Thread timerThread;
	private Main main;
	
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
		
	}
	
	@FXML
	public void show_about(Event e) {
		
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
			resetBtn.getStyleClass().clear();
			resetBtn.getStyleClass().add("gameend");
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
