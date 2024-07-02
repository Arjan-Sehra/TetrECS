package uk.ac.soton.comp1206.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.multimedia.Multimedia;
import uk.ac.soton.comp1206.ui.PieceBoard;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private GamePiece currentPiece;

    private static final Logger logger = LogManager.getLogger(Game.class);

    private int score = 0;
    private int level = 0;
    private int lives = 3;
    private int scoreLeftToNextLevel = 1000;
    private int multiplier = 1;

    private NextPieceListener nextPieceListener;

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    /**
     * Following Piece is going to be the 2nd piece you see, so after you play the current piece, you know what piece is coming next
     */
    private GamePiece followingPiece;

    /**
     * Two "temporary" pieces, to allow us to swap the current piece with the following piece and allow us to update them both
     */
    private GamePiece temporaryPieceOne;
    private GamePiece temporaryPieceTwo;

    private PieceBoard pieceBoard;

    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols,rows);

        //Makes the current piece a random gamePiece that has been spawned
        this.currentPiece = this.spawnPiece();
    }

    //Listener should be called when next piece is generated.
    public void setNextPieceListener() {
        
    }

    //to be called when the user pressed either the right click or r on the keyboard
    public void rotateCurrentPiece(GamePiece gamePiece) {
        gamePiece.rotate();
    }

    /**
     * Current piece and following piece into 2 temporary variables to ensure we can swap the currentPiece and followingPiece
     */
    public void swapCurrentPiece() {
        this.temporaryPieceOne = this.currentPiece;
        this.temporaryPieceTwo = this.followingPiece;
        this.currentPiece = this.temporaryPieceTwo;
        this.followingPiece = this.temporaryPieceOne;
    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();

        logger.info("Attempting background music");
        Multimedia.playBackgroundMusic("music/game_start.wav");
    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");

        //When game is initialised, spawn a new GamePiece and set it to the currentPiece
        this.currentPiece = this.spawnPiece();
    }

    public void resetScoreLeftToNextLevel() {
        this.scoreLeftToNextLevel = 1000;
    }

    public void decreaseScoreToNextLevel(int points) {
        this.scoreLeftToNextLevel -= points;
    }

    public void shouldLevelIncrease() {
        if (this.scoreLeftToNextLevel <= 0) {
            this.increaseLevel();
            if (this.scoreLeftToNextLevel == 0) {
                this.resetScoreLeftToNextLevel();
            } else {
                int remainder = this.scoreLeftToNextLevel * -1;
                this.resetScoreLeftToNextLevel();
                this.decreaseScoreToNextLevel(remainder);
            }
        }
    }

    //SCORE METHODS
    public void increaseScore(int numberOfLines, int numberOfBlocks) {
        int formula = numberOfLines * numberOfBlocks * 10 * this.multiplier;
        this.score += formula;
        this.decreaseScoreToNextLevel(formula);
    }

    public int getScore() {
        return this.score;
    }

    //not sure if I will need this method but no harm in adding it:
    public void resetScore() {
        this.score = 0;
    }

    //LEVEL METHODS
    public int getLevel() {
        return this.level;
    }

    public void increaseLevel() {
        this.level += 1;
    }

    //not sure if I will need this method but no harm in adding it:
    public void resetLevel() {
        this.level = 0;
    }

    //LIVES METHODS
    public void decreaseLives() {
        this.lives -= 1;
    }

    public void increaseLives() {
        this.lives += 1;
    }

    //not sure if I will need this method but no harm in adding it:
    public void resetLives() {
        this.lives = 3;
    }

    //MULTIPLIER METHODS
    public int getMultiplier() {
        return this.multiplier;
    }

    public void increaseMultiplier(int value) {
        this.multiplier += value;
    }

    public void resetMultiplier() {
        this.multiplier = 1;
    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        //Get the new value for this block
        int previousValue = grid.get(x,y);
        int newValue = previousValue + 1;
        if (newValue  > GamePiece.PIECES) {
            newValue = 0;
        }

        //Update the grid with the new value
        grid.set(x,y,newValue);

        //fetches the next piece
        this.nextPiece();

        //call setNextPieceListener when next piece is generated
        this.setNextPieceListener();

        //Called after a piece has been placed to clear lines
        this.afterPiece();

        //method to work out if the level should increase
        this.shouldLevelIncrease();
    }

    //returns a random GamePiece
    public GamePiece spawnPiece() {
        //this should create a random number between 0 and 14 to return a random GamePiece
        int min = 0;
        int max = 14;
        int randomInteger = (int)(Math.random()*(max-min+1) + min);
        return GamePiece.createPiece(randomInteger);
    }

    //replaces the current piece with a new piece and replaces following piece with a random piece
    public void nextPiece() {
        this.currentPiece = this.followingPiece;
        this.followingPiece = this.spawnPiece();
    }

    //Clears any lines that have been made
    public void afterPiece() {
        ArrayList<Integer> integerArrayList = new ArrayList<Integer>();
        ArrayList<String> stringArrayList = new ArrayList<String>();

        int numberOfLinesCleared = 0;
        int numberOfGridBlocksCleared = 0;

        //checks horizontal top row
        var firstRow = 0;
        for (int row=0; row < 1; row++) {
            for (int col=0; col < this.getCols(); col++) {
                //logger.info("This is grid.get(col, row) " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                     firstRow += 1;
                }
            }
        }
        if (firstRow == 5) {
            stringArrayList.add("firstRow");
        }
        integerArrayList.add(firstRow);

        //checks horizontal 2nd row
        var secondRow = 0;
        for (int row=1; row < 2; row++) {
            for (int col=0; col < this.getCols(); col++) {
                //logger.info("2nd Row: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    secondRow += 1;
                }
            }
        }
        if (secondRow == 5) {
            stringArrayList.add("secondRow");
        }
        integerArrayList.add(secondRow);

        //checks horizontal 3rd row
        var thirdRow = 0;
        for (int row=2; row < 3; row++) {
            for (int col=0; col < this.getCols(); col++) {
                //logger.info("2nd Row: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    thirdRow += 1;
                }
            }
        }
        if (thirdRow == 5) {
            stringArrayList.add("thirdRow");
        }
        integerArrayList.add(thirdRow);

        //checks horizontal 4th row
        var fourthRow = 0;
        for (int row=3; row < 4; row++) {
            for (int col=0; col < this.getCols(); col++) {
                //logger.info("2nd Row: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    fourthRow += 1;
                }
            }
        }
        if (fourthRow == 5) {
            stringArrayList.add("fourthRow");
        }
        integerArrayList.add(fourthRow);

        //checks horizontal last row
        var lastRow = 0;
        for (int row=4; row < 5; row++) {
            for (int col=0; col < this.getCols(); col++) {
                //logger.info("2nd Row: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    lastRow += 1;
                }
            }
        }
        if (lastRow == 5) {
            stringArrayList.add("lastRow");
        }
        integerArrayList.add(lastRow);

        //checks vertical 1st column
        var firstCol = 0;
        for (int col=0; col < 1; col++) {
            for (int row=0; row < this.getRows(); row++) {
                //logger.info("1st column: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    firstCol += 1;
                }
            }
        }
        if (firstCol == 5) {
            stringArrayList.add("firstCol");
        }
        integerArrayList.add(firstCol);

        //checks vertical 2nd column
        var secondCol = 0;
        for (int col=1; col < 2; col++) {
            for (int row=0; row < this.getRows(); row++) {
                //logger.info("1st column: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    secondCol += 1;
                }
            }
        }
        if (secondCol == 5) {
            stringArrayList.add("secondCol");
        }
        integerArrayList.add(secondCol);

        //checks vertical 3rd column
        var thirdCol = 0;
        for (int col=2; col < 3; col++) {
            for (int row=0; row < this.getRows(); row++) {
                //logger.info("1st column: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    thirdCol += 1;
                }
            }
        }
        if (thirdCol == 5) {
            stringArrayList.add("thirdCol");
        }
        integerArrayList.add(thirdCol);

        //checks vertical 4th column
        var fourthCol = 0;
        for (int col=3; col < 4; col++) {
            for (int row=0; row < this.getRows(); row++) {
                //logger.info("1st column: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    fourthCol += 1;
                }
            }
        }
        if (fourthCol == 5) {
            stringArrayList.add("fourthCol");
        }
        integerArrayList.add(fourthCol);

        //checks vertical last column
        var lastCol = 0;
        for (int col=4; col < 5; col++) {
            for (int row=0; row < this.getRows(); row++) {
                //logger.info("1st column: " + grid.get(col, row));
                if (grid.get(col, row) >= 1) {
                    lastCol += 1;
                }
            }
        }
        if (lastCol == 5) {
            stringArrayList.add("lastCol");
        }
        integerArrayList.add(lastCol);

        //checks bottom right to top left diagonal
        var diagonalOne = 0;
        if (grid.get(4, 4) >= 1) {
            diagonalOne += 1;
            if (grid.get(3,3) >= 1) {
                diagonalOne += 1;
                if (grid.get(2,2) >= 1) {
                    diagonalOne += 1;
                    if (grid.get(1,1) >= 1) {
                        diagonalOne += 1;
                        if (grid.get(0,0) >= 1) {
                            diagonalOne += 1;
                        }
                    }
                }
            }
        }
        if (diagonalOne == 5) {
            stringArrayList.add("diagonalOne");
        }
        integerArrayList.add(diagonalOne);

        //checks bottom left to top right diagonal
        var diagonalTwo = 0;
        if (grid.get(0, 4) >= 1) {
            diagonalTwo += 1;
            if (grid.get(1,3) >= 1) {
                diagonalTwo += 1;
                if (grid.get(2,2) >= 1) {
                    diagonalTwo += 1;
                    if (grid.get(3,1) >= 1) {
                        diagonalTwo += 1;
                        if (grid.get(4,0) >= 1) {
                            diagonalTwo += 1;
                        }
                    }
                }
            }
        }
        if (diagonalTwo == 5) {
            stringArrayList.add("diagonalTwo");
        }
        integerArrayList.add(diagonalTwo);

        //A for loop that goes through the lines with 5 blocks in them and deletes that row/col/diagonal line
        for (int i=0; i < stringArrayList.size(); i++) {
            clearingLines(stringArrayList.get(i));
        }

        //for loop to go through the list and see how many of the rows/cols/diagonals have 5 in it
        int count = 0;
        for (int i=0; i < integerArrayList.size(); i++) {
            if (integerArrayList.get(i) == 5) {
                count += 1;
            }
        }

        //the number of lines cleared is the number of rows/cols/diagonals that have 5 blocks inside of it
        numberOfLinesCleared = count;
        if (numberOfLinesCleared == 0) {
            numberOfGridBlocksCleared = 0;
            logger.info("No lines were cleared");
            this.increaseScore(numberOfLinesCleared, numberOfGridBlocksCleared);
            this.resetMultiplier();
        } else if (numberOfLinesCleared == 1) {
            logger.info("At least 1 line was cleared");
            numberOfGridBlocksCleared = 5;
            logger.info("Number Of Grid Blocks Cleared = " + numberOfGridBlocksCleared);
            logger.info("Number of Lines Cleared = " + numberOfLinesCleared);
            logger.info("Score before score method = " + this.score);
            logger.info("Level = " + this.level);
            this.increaseScore(numberOfLinesCleared, numberOfGridBlocksCleared);
            logger.info("score after increased score = " + this.score);
            this.increaseMultiplier(1);
        } else if (numberOfLinesCleared == 2) {
            numberOfGridBlocksCleared = 9;
            this.increaseScore(numberOfLinesCleared, numberOfGridBlocksCleared);
            this.increaseMultiplier(2);
        } else if (numberOfLinesCleared == 3) {
            numberOfGridBlocksCleared = 13;
            this.increaseScore(numberOfLinesCleared, numberOfGridBlocksCleared);
            this.increaseMultiplier(3);
        } else if (numberOfLinesCleared == 4) {
            numberOfGridBlocksCleared = 17;
            this.increaseScore(numberOfLinesCleared, numberOfGridBlocksCleared);
            this.increaseMultiplier(4);
        } else {
            this.increaseScore(numberOfLinesCleared, numberOfGridBlocksCleared);
        }
    }

    //takes a string and uses the name to delete that row/column/diagonal line
    public void clearingLines(String s) {
        if (s == "firstRow") {
            for (int col=0; col < this.getCols(); col++) {
                grid.set(col, 0, 0);
            }
        } else if (s == "secondRow") {
            for (int col=0; col < this.getCols(); col++) {
                grid.set(col, 1, 0);
            }
        } else if (s == "thirdRow") {
            for (int col=0; col < this.getCols(); col++) {
                grid.set(col, 2, 0);
            }
        } else if (s == "fourthRow") {
            for (int col=0; col < this.getCols(); col++) {
                grid.set(col, 3, 0);
            }
        } else if (s == "lastRow") {
            for (int col=0; col < this.getCols(); col++) {
                grid.set(col, 4, 0);
            }
        } else if (s == "firstCol") {
            for (int row=0; row < this.getRows(); row++) {
                grid.set(0, row, 0);
            }
        } else if (s == "secondCol") {
            for (int row=0; row < this.getRows(); row++) {
                grid.set(1, row, 0);
            }
        } else if (s == "thirdCol") {
            for (int row=0; row < this.getRows(); row++) {
                grid.set(2, row, 0);
            }
        } else if (s == "fourthCol") {
            for (int row=0; row < this.getRows(); row++) {
                grid.set(3, row, 0);
            }
        } else if (s == "lastCol") {
            for (int row=0; row < this.getRows(); row++) {
                grid.set(4, row, 0);
            }
        } else if (s == "diagonalOne") {
            for (int rowcol=0; rowcol < this.getRows(); rowcol++) {
                grid.set(rowcol, rowcol, 0);
            }
        } else if (s == "diagonalTwo") {
            grid.set(0, 4, 0);
            grid.set(1,3,0);
            grid.set(2,2,0);
            grid.set(3,1,0);
            grid.set(4,0,0);
        }
    }

    /**
     * Get the grid model inside this game representing the game state of the board
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Get the number of columns in this game
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }
}