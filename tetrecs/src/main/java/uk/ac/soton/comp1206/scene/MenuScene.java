package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.multimedia.Multimedia;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    private NextPieceListener nextPieceListener;

    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
        logger.info("Intro music");
        Multimedia.playAudioFile("sounds/intro.mp3");
        logger.info("Menu background music");
        Multimedia.playBackgroundMusic("music/menu.mp3");
    }

    //Create a NextPieceListener in ChallengeScene to listen to new pieces inside the game and call an appropriate method
    public void setNextPieceListener() {

    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        //new stack pane
        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);

        //new border pane
        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);

        //title
        var title = new Text("TetrECS");
        title.getStyleClass().add("bigtitle");

        mainPane.setTop(title);

        //new vbox pane
        VBox vBox = new VBox();

        int gameWidth = gameWindow.getWidth();
        int gameHeight = gameWindow.getHeight();

        vBox.setPadding(new Insets(gameHeight/2, gameHeight/2, gameWidth/2 - 50, gameWidth/2 - 50));
        vBox.setPrefWidth(50);
        vBox.setPrefHeight(50);
        vBox.setSpacing(25);

        //basic button
        var button = new Button("Play");
        //basic button
        var instructions = new Button("Instructions");

        menuPane.setAlignment(title, Pos.TOP_CENTER);
        menuPane.setAlignment(button, Pos.CENTER);
        menuPane.setAlignment(instructions, Pos.BOTTOM_CENTER);

        vBox.getChildren().addAll(title, button, instructions);

        VBox.setVgrow(mainPane, Priority.ALWAYS);
        menuPane.getChildren().add(vBox);

        //Bind the button action to the startGame method in the menu
        button.setOnAction(this::startGame);
        //Bind the button action to the loadInstructions method in the menu
        instructions.setOnAction(this::loadInstructions);
    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
        logger.info("Attempting to start loading screen first");
        gameWindow.startLoadingScene();

        gameWindow.setupStage();
    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    private void startGame(ActionEvent event) {
        gameWindow.startChallenge();
    }

    /**
     * Handle when the Instructions button is pressed
     * @param event
     */
    private void loadInstructions(ActionEvent event) {
        gameWindow.cleanup();
        gameWindow.startInstructions();
    }

}
