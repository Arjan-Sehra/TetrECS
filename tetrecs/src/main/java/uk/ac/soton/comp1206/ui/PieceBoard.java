package uk.ac.soton.comp1206.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.game.GamePiece;

/**
 * The PieceBoard class extends GameBoard and should be able to display grids of GamePiece's
 */
public class PieceBoard extends GameBoard {

    private static final Logger logger = LogManager.getLogger(PieceBoard.class);

    public PieceBoard(int cols, int rows, double width, double height) {
        super(3, 3, width, height);
    }

    public void displayPiece(int pieceNumber) {
        GamePiece gamePiece = GamePiece.createPiece(pieceNumber);
        this.build();
        logger.info("gamePiece: " + gamePiece);
    }

}