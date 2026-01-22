package org.depaul.logic.board;

import org.depaul.logic.data.Score;
import org.depaul.logic.data.ViewData;

public interface Board {

    // Existing Ones
    boolean createNewBrick();
    int[][] getBoardMatrix();
    ViewData getViewData();
    void mergeBrickToBackground();
    Score getScore();
    void newGame();

    // New Ones
    boolean tryMoveLeft();
    boolean tryMoveRight();
    boolean tryMoveDown(); // returns true if moved one row; false if blocked (then caller locks)
    boolean rotateCW();  // returns true if rotation applied
    boolean rotateCCW(); // returns true if rotation applied
    int hardDrop();  // returns number of rows descended until floor/stack
    int clearFullLines(); // returns number of cleared lines
}
