package org.depaul.logic.events;

import org.depaul.gui.GuiController;
import org.depaul.gui.ScoreBoardManager;
import org.depaul.logic.board.Board;
import org.depaul.logic.board.SimpleBoard;
import org.depaul.logic.data.Score;
import org.depaul.logic.data.ViewData;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(25, 10);
    private final ScoreBoardManager scoreboard = new ScoreBoardManager(new ArrayList<>());

    private final GuiController viewGuiController;

    private final int MAX_GRAVITY_DELAY = 500;
    private final int MIN_GRAVITY_DELAY = 100;

    private final int MAX_LOCK_DELAY = 500;
    private final int MIN_LOCK_DELAY = 200;
    private int lockDelay = MAX_LOCK_DELAY;
    private long lockTime = -1;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindLevel(board.getScore().levelProperty());
        viewGuiController.bindLines(board.getScore().totalLinesClearedProperty());
    }

    @Override
    public ViewData onMoveEvent(MoveEvent event) {
        boolean changed = false;    // tracks whether visible change occurred
        switch (event.getEventType()) {
            case LEFT:
                // Tries to move the current brick to the left, if possible returns true
                changed = board.tryMoveLeft();
                break;

            case RIGHT:
                // Tries to move the current brick to the right, if possible returns true
                changed = board.tryMoveRight();
                break;

            case DOWN:
                // User pressed the down arrow or S, tries to move the brick down one
                if (board.tryMoveDown()) {
                    if (event.getEventSource() == EventSource.USER) {
                        board.getScore().add(1);           // +1 soft drop point
                    }
                    changed = true;
                    lockTime = -1;
                } else {
                    updateLockDelay();
                    changed = true;
                }
                break;

            case ROTATE_CW:
                // Tries to rotate clockwise
                changed = board.rotateCW();
                break;

            case ROTATE_CCW:
                // Tries to rotate counterclockwise
                changed = board.rotateCCW();
                break;

            case HARD_DROP:
                // Drops brick to the floor, score = 2 * rows fallen
                int rows = board.hardDrop();   // returns rows descended
                if (rows > 0) {
                    board.getScore().add(rows * 2);
                }
                // After hard drop, locks brick into background, clear lines, and spawn next
                lockClearAndSpawn();
                lockTime = -1;
                changed = true;
                break;
        }
        // Need to refresh the GUI with the view data
        return board.getViewData();
    }

    private void updateLockDelay() {
        if (lockTime == -1) {
            lockTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - lockTime >= lockDelay) {
            lockClearAndSpawn();
            lockTime = -1;
        }
    }

    private void lockClearAndSpawn() {
        board.mergeBrickToBackground();  // Makes current brick part of the board

        // Checks for any full lines created by this brick
        int cleared = board.clearFullLines();   // Return number of lines; add if missing
        if (cleared > 0) {
            // Use enhanced Tetris scoring system instead of simple 100 points per line
            board.getScore().addLinesClearScore(cleared);
        }

        // Tries to spawn next brick. If true, that means the brick collides and game over
        if (board.createNewBrick()) {
            Score snap = new Score();
            snap.scoreProperty().set(board.getScore().scoreProperty().get());
            scoreboard.addScore(snap);

            viewGuiController.gameOver();
            viewGuiController.showLeaderboard(scoreboard.getTopScores());
            return;
        }

        // Update background
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        int level = board.getScore().getLevel();
        // Decrease gravityDelay as level increases
        viewGuiController.setGravityDelay(Math.max(MIN_GRAVITY_DELAY, MAX_GRAVITY_DELAY - ((level - 1) * 40)));

        // Decrease lockDelay as level increases
        lockDelay = Math.max(MIN_LOCK_DELAY, MAX_LOCK_DELAY - ((level - 1) * 30));
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.setGravityDelay(MAX_GRAVITY_DELAY);
        lockTime = -1;
        lockDelay = MAX_LOCK_DELAY;
    }
}
