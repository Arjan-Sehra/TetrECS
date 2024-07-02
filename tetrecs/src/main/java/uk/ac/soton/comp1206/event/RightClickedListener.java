package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.component.GameBlock;

/**
 * The Right Clicked Listener is used to handle the event when a block in GameBoard is right clicked.
 */
public interface RightClickedListener {

    public void setOnRightClicked(GameBlock gameBlock);

}