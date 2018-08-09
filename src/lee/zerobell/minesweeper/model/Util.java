package lee.zerobell.minesweeper.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lee.zerobell.minesweeper.Main;

public class Util {

	static final int OPEN = 1;
	static final int CLOSED = 2;
	static final int QUESTION = 3;
	static final int MARKED = 4;
	
	public static Image MarkedBitmap;
	public static Image QuestionBitmap;
	public static Image MineBitmap;
	
	public static ImageView GameReadyBitmap;
	public static ImageView GameOverBitmap;
	public static ImageView GameEndBitmap;
	
	public static Image[] numbersBitmap;
	
	public static final int READY = 5;
	public static final int STARTED = 6;
	public static final int GAMEOVER = 7;
	
	static final int MINE = -1;
	
	public static final int TILE_WIDTH = 25;
	public static final int TILE_HEIGHT = 25;
	
	public static final int[] MODE_EASY = {9, 9, 10};
	public static final int[] MODE_NORMAL = {16, 16, 40};
	public static final int[] MODE_HARD = {30, 16, 99};
	
	public static final int NOT_CLICKED = 0;
	public static final int PRIMARY_CLICKED = 1;
	public static final int SECONDARY_CLICKED = 2;
	public static final int DUAL_CLICKED = 3;
	
	public static final int MAX_ROW = 24;
	public static final int MAX_COL = 30;
	public static final int MAX_MINE = 667;
	
	public static final int MIN_ROW = 9;
	public static final int MIN_COL = 9;
	public static final int MIN_MINE = 10;
	
	static public void setImages() {
		Image img1 = new Image(Main.class.getResource("view/game_ready.png").toString());
		Image img2 = new Image(Main.class.getResource("view/game_over.png").toString());
		Image img3 = new Image(Main.class.getResource("view/game_end.png").toString());
		
		GameReadyBitmap = new ImageView(img1);
		GameOverBitmap = new ImageView(img2);
		GameEndBitmap = new ImageView(img3);
		
		MarkedBitmap = new Image(Main.class.getResource("view/marked.png").toString());
		QuestionBitmap = new Image(Main.class.getResource("view/question.png").toString());
		MineBitmap = new Image(Main.class.getResource("view/mine.png").toString());
		
		numbersBitmap = new Image[8];
		
		for (int i=0; i<8; i++) {
			numbersBitmap[i] = new Image(Main.class.getResource("view/" + Integer.toString(i+1) + ".png").toString());
		}
	}
}
