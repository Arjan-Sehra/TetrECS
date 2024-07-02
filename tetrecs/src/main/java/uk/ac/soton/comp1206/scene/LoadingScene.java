package uk.ac.soton.comp1206.scene;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * Class is the loading screen before we arrive at the menu
 */
public class LoadingScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(LoadingScene.class);

    private ImageView logo;

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     * @param gameWindow the game window
     */
    public LoadingScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Loading Scene");
    }

    @Override
    public void build() {
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());
        var loadingPane = new StackPane();
        loadingPane.setMaxWidth(gameWindow.getWidth());
        loadingPane.setMaxHeight(gameWindow.getHeight());
        loadingPane.getStyleClass().add("intro");
        root.getChildren().add(loadingPane);

        var mainPane = new BorderPane();
        loadingPane.getChildren().add(mainPane);

        animateLogo();
    }

    public void animateLogo() {
        Image ecsLogo = new Image("/coursework/tetrecs/src/main/resources/images/ECSGames.png");
        logo.setImage(ecsLogo);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(5000), logo);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    @Override
    public void initialise() {
        gameWindow.setupStage();
    }

}