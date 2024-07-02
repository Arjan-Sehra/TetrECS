package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.game.GamePiece;

/**
 * The Next Piece Listener is used to change the nextPiece and the followingPiece in game (when a piece is placed).
 */
public interface NextPieceListener {

    public void nextPiece(GamePiece gamePiece);

    public void followingPiece(GamePiece gamePiece);
}