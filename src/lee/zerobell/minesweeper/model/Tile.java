package lee.zerobell.minesweeper.model;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lee.zerobell.minesweeper.MineSweeperController;

public class Tile extends Button {

	private int state;
	private int content;
	private int clickedState;

	static private MineModel mineModel;
	static private MineSweeperController mineSweeperController;

	private int col, row;

	public Tile() {
		this(0, 0);
	}

	public Tile(int col, int row) {
		super();
		this.setPrefSize(Util.TILE_WIDTH, Util.TILE_HEIGHT);
		this.setMaxSize(Util.TILE_WIDTH, Util.TILE_HEIGHT);
		this.setMinSize(Util.TILE_WIDTH, Util.TILE_HEIGHT);
		this.content = 0;
		this.state = Util.CLOSED;
		this.col = col;
		this.row = row;
		this.clickedState = Util.NOT_CLICKED;

		this.setFont(Font.font("Verdana", FontWeight.BOLD, 7));
		getStyleClass().add("closed");
	}

	public void setContent(int content) {
		this.content = content;
	}

	public int getContent() {
		return this.content;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setIndex(int col, int row) {
		this.col = col;
		this.row = row;
	}

	public void setClickedState(int state) {
		this.clickedState = state;
	}

	public int getClickedState() {
		return this.clickedState;
	}

	public int open() {
		return open(false);
	}

	public int open(boolean ripple) {
		if (this.state == Util.CLOSED) {
			setState(Util.OPEN);
			mineModel.addOpenedTiles();
			if (this.content == Util.MINE) {
				refresh();
				mineSweeperController.gameOver(false);
				return Util.MINE;
			} else if (this.content == 0){
				Tile[] nearTile = mineModel.getNearTiles(col, row);
				for (Tile element : nearTile) {
					element.open(false);
				}
			}
			// this.setDisable(true);
			getStyleClass().clear();
			getStyleClass().add("opened");
		}
		else if ((this.state == Util.OPEN)&&(ripple)) {
			int nearMarked = 0;
			Tile[] nearTiles = mineModel.getNearTiles(col, row);
			for (Tile e : nearTiles) {
				if (e.state == Util.MARKED)
					nearMarked++;
			}
			if (content == nearMarked) {
				for (Tile e : nearTiles) {
					e.open(false);
				}
			}
		}
		refresh();
		if (mineModel.getState() == Util.READY) {
			mineModel.setState(Util.STARTED);
			mineSweeperController.startTimer();
		}
		return 0;
	}

	public void refresh() {
		if (state == Util.OPEN) {
			if (content == Util.MINE) {
				getStyleClass().clear();
				getStyleClass().add("marked");
				ImageView imageView = new ImageView(Util.MineBitmap);
				this.setGraphic(imageView);
			}
			else if (content == 0) {

			} else {
				ImageView imageView = new ImageView(Util.numbersBitmap[content - 1]);
				this.setGraphic(imageView);
			}
		} else if (state == Util.MARKED) {
			ImageView imageView = new ImageView(Util.MarkedBitmap);
			this.setGraphic(imageView);
		} else if (state == Util.QUESTION) {
			ImageView imageView = new ImageView(Util.QuestionBitmap);
			this.setGraphic(imageView);
		} else if (state == Util.CLOSED)
			this.setGraphic(null);
		// SetGraphic()
	}

	public void mark() {
		switch (this.state) {
		case Util.MARKED:
			setState(Util.QUESTION);
			mineSweeperController.addMine();
			getStyleClass().clear();
			getStyleClass().add("question");
			break;
		case Util.QUESTION:
			setState(Util.CLOSED);
			getStyleClass().clear();
			getStyleClass().add("closed");
			break;
		case Util.CLOSED:
			setState(Util.MARKED);
			mineSweeperController.subtractMine();
			getStyleClass().clear();
			getStyleClass().add("marked");
			break;
		default:
			break;
		}
		refresh();
	}

	public static void setModel(MineModel model) {
		mineModel = model;
	}

	public static void setController(MineSweeperController controller) {
		mineSweeperController = controller;
	}
}
