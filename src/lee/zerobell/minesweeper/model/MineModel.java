package lee.zerobell.minesweeper.model;

import java.util.ArrayList;
import java.util.Random;

import lee.zerobell.minesweeper.MineSweeperController;

public class MineModel {

	private MineSweeperController mineSweeperController;
	
	private int state;
	private int timer;
	private Tile[][] TileArr;
	private int rows, cols, mines;
	
	private int openedTiles;
	private int endCondition;
	
	public MineModel() {
		this(Util.MODE_EASY);
	}
	
	public MineModel(int[] setting) {
		this.cols = setting[0];
		this.rows = setting[1];
		this.mines = setting[2];
		
		this.timer = 0;
		this.openedTiles = 0;
		this.endCondition = cols*rows - mines;
	}
	
	public void setCols(int cols) {
		this.cols = cols;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public void setMines(int mines) {
		this.mines = mines;
	}
	
	public void setController(MineSweeperController controller) {
		this.mineSweeperController = controller;
	}
	
	public void setMode(int[] setting) {
		this.cols = setting[0];
		this.rows = setting[1];
		this.mines = setting[2];
		this.endCondition = cols*rows - mines;
	}
	
	
	public int getTimer() {
		return this.timer;
	}
	
	public void addTimer() {
		this.timer++;
	}
	
	public void addOpenedTiles() {
		this.openedTiles++;
		if (isGameEnded()) { mineSweeperController.gameOver(true);
		System.out.println("Game End");}
	}
	
	public boolean isGameEnded() {
		return (openedTiles==endCondition);
	}
	
	public int getState() {
		return this.state;
	}
	
	public int getCols() {
		return this.cols;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getMines() {
		return this.mines;
	}
	
	public Tile[][] getTiles() {
		return this.TileArr;
	}
	
	public void setState(int state) {
		this.state = state;
		if (state == Util.GAMEOVER) {
			for (Tile[] element : TileArr) {
				for (Tile e : element) {
					e.setDisable(true);
				}
			}
		}
	}
	
	public void createTiles() {
		TileArr = new Tile[cols][rows];
		for (int i = 0; i<cols; i++) {
			for (int j = 0; j<rows; j++) {
				TileArr[i][j] = new Tile(i, j);
			}
		}
	}
	
	public void init() {
		createTiles();
		setupMines();
		this.state = Util.READY;
		this.timer = 0;
		openedTiles = 0;
	}
	
	public void setupMines() {
		Random random = new Random();
		int maxSize = rows*cols;
		ArrayList<Integer> mineArr = new ArrayList<Integer>();
		for (int i = 0; i<mines; i++) {
			int randInt;
			while (true) {
				randInt = random.nextInt(maxSize);
				if (mineArr.contains(randInt)) continue;
				else break;
			}
			TileArr[randInt/rows][randInt%rows].setContent(Util.MINE);
			mineArr.add(randInt);
		}
		
		for (int i = 0; i<cols; i++) {
			for (int j = 0; j<rows; j++) {
				int contentNum = 0;
				if (TileArr[i][j].getContent()==Util.MINE) continue;
				else {
					Tile[] nearTiles = getNearTiles(i, j);
					for (Tile element : nearTiles) {
						if (element.getContent() == Util.MINE)
							contentNum++;
					}
				}
				TileArr[i][j].setContent(contentNum);
			}
		}
	}
	
	public Tile[] getNearTiles(int col, int row) {
		Tile[] resultTile;
		if (row == 0) {
			if (col == 0) {
				resultTile = new Tile[3];
				resultTile[0] = TileArr[1][0];
				resultTile[1] = TileArr[1][1];
				resultTile[2] = TileArr[0][1];
			}
			else if (col == cols-1) {
				resultTile = new Tile[3];
				resultTile[0] = TileArr[cols-2][0];
				resultTile[1] = TileArr[cols-2][1];
				resultTile[2] = TileArr[cols-1][1];
			}
			else {
				resultTile = new Tile[5];
				resultTile[0] = TileArr[col-1][0];
				resultTile[1] = TileArr[col-1][1];
				resultTile[2] = TileArr[col][1];
				resultTile[3] = TileArr[col+1][1];
				resultTile[4] = TileArr[col+1][0];
			}
		}
		else if (row == rows-1) {
			if (col == 0) {
				resultTile = new Tile[3];
				resultTile[0] = TileArr[1][row];
				resultTile[1] = TileArr[1][row-1];
				resultTile[2] = TileArr[0][row-1];
			}
			else if (col == cols-1) {
				resultTile = new Tile[3];
				resultTile[0] = TileArr[col-1][row];
				resultTile[1] = TileArr[col-1][row-1];
				resultTile[2] = TileArr[col][row-1];
			}
			else {
				resultTile = new Tile[5];
				resultTile[0] = TileArr[col-1][row];
				resultTile[1] = TileArr[col-1][row-1];
				resultTile[2] = TileArr[col][row-1];
				resultTile[3] = TileArr[col+1][row-1];
				resultTile[4] = TileArr[col+1][row];
			}
		}
		else {
			if (col == 0) {
				resultTile = new Tile[5];
				resultTile[0] = TileArr[col][row-1];
				resultTile[1] = TileArr[col+1][row-1];
				resultTile[2] = TileArr[col+1][row];
				resultTile[3] = TileArr[col+1][row+1];
				resultTile[4] = TileArr[col][row+1];
			}
			else if (col == cols-1) {
				resultTile = new Tile[5];
				resultTile[0] = TileArr[col][row-1];
				resultTile[1] = TileArr[col-1][row-1];
				resultTile[2] = TileArr[col-1][row];
				resultTile[3] = TileArr[col-1][row+1];
				resultTile[4] = TileArr[col][row+1];
			}
			else {
				resultTile = new Tile[8];
				resultTile[0] = TileArr[col-1][row-1];
				resultTile[1] = TileArr[col][row-1];
				resultTile[2] = TileArr[col+1][row-1];
				resultTile[3] = TileArr[col-1][row];
				resultTile[4] = TileArr[col+1][row];
				resultTile[5] = TileArr[col-1][row+1];
				resultTile[6] = TileArr[col][row+1];
				resultTile[7] = TileArr[col+1][row+1];
			}
		}
		
		return resultTile;
	}
}
