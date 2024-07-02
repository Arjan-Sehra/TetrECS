package uk.ac.soton.comp1206.scene;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.ui.PieceBoard;

/**
 * Class displays a screen on how to play the game, when the instructions button is clicked
 */
public class InstructionsScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(InstructionsScene.class);

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public InstructionsScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Instructions Scene");
    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());

        //creating a stack pane
        var backgroundPane = new StackPane();

        //creating a borderpane
        var instructionsPane = new BorderPane();
        instructionsPane.setMaxWidth(gameWindow.getWidth());
        instructionsPane.setMaxHeight(gameWindow.getHeight());
        instructionsPane.getStyleClass().add("instructionsBackground");

        root.getChildren().add(instructionsPane);
        root.getChildren().add(backgroundPane);

        //title for instructions screen
        var title = new Text("How To Play");
        title.getStyleClass().add("heading");
        backgroundPane.setAlignment(title, Pos.TOP_CENTER);
        backgroundPane.getChildren().add(title);

        //new grid pane
        var gridPane = new GridPane();

        //Creating loads of piece boards, which will be used to display the different blocks available in the game
        var pieceBoard = new PieceBoard(3, 3, 10, 10);
        pieceBoard.displayPiece(0);
        pieceBoard.displayPiece(1);
        pieceBoard.displayPiece(2);
        pieceBoard.displayPiece(3);
        pieceBoard.displayPiece(4);
        pieceBoard.displayPiece(5);
        pieceBoard.displayPiece(6);
        pieceBoard.displayPiece(7);
        pieceBoard.displayPiece(8);
        pieceBoard.displayPiece(9);
        pieceBoard.displayPiece(10);
        pieceBoard.displayPiece(11);
        pieceBoard.displayPiece(12);
        pieceBoard.displayPiece(13);
        pieceBoard.displayPiece(14);

        //new hbox
        var hbox = new HBox();

        logger.info("Attempting to set on press keyed thing");

        instructionsPane.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            escape();
        });

        backgroundPane.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            escape();
        });

    }

    /**
     * Initialises the Instructions section
     */
    @Override
    public void initialise() {
        gameWindow.setupStage();
    }

    public void keyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            escape();
        }
    }

    /**
     * When ESC is pressed
     */
    private void escape() {
        logger.info("Should return to Menu");
        gameWindow.cleanup();
        gameWindow.startMenu();
    }

}