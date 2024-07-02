package uk.ac.soton.comp1206.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.ui.PieceBoard;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;

    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        setupGame();

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        //creating a new stack pane
        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        //using .css to create the background using the "menu-background" on the file
        challengePane.getStyleClass().add("menu-background");

        //Showing the score, level, multiplier and lives
        //creating a vbox
        VBox vbox = new VBox();
        //vbox being the size of the game window
        int gameWidth = gameWindow.getWidth();
        int gameHeight = gameWindow.getHeight();
        vbox.setPadding(new Insets(gameHeight - 20, gameHeight - 20, gameWidth - 15, gameWidth - 15));
        vbox.setPrefHeight(100);
        vbox.setPrefWidth(40);

        var score = new Text("Score: " + game.getScore());
        score.getStyleClass().add("score");
        var level = new Text("Level: " + game.getLevel());
        level.getStyleClass().add("level");
        var lives = new Text("Lives: " + game.getLevel());
        lives.getStyleClass().add("lives");
        var multiplier = new Text("Multiplier: " + game.getMultiplier());
        multiplier.getStyleClass().add("multiplier");

        //creating an empty grid at the bottom right of the screen (we will be making this display the following piece)
        var pieceBoard = new PieceBoard(3, 3, gameWindow.getWidth()/4, gameWindow.getHeight()/4);
        StackPane.setAlignment(pieceBoard, Pos.BOTTOM_RIGHT);

        challengePane.setAlignment(score, Pos.TOP_RIGHT);
        challengePane.setAlignment(level, Pos.CENTER_RIGHT);
        challengePane.setAlignment(lives, Pos.BOTTOM_RIGHT);
        challengePane.setAlignment(multiplier, Pos.TOP_LEFT);

        vbox.getChildren().addAll(score, level, lives, multiplier);
        vbox.setSpacing(10);

        challengePane.getChildren().addAll(new ImageView(), pieceBoard, score, level, lives, multiplier);

        root.getChildren().add(challengePane);

        //creating a border pane
        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);

        var board = new GameBoard(game.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
        mainPane.setCenter(board);

        //Handle block on game board grid being clicked
        board.setOnBlockClick(this::blockClicked);
    }

    /**
     * Handle when a block is clicked
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5);

    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");
        game.start();
    }

}
