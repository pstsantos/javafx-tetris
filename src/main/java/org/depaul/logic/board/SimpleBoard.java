package org.depaul.logic.board;

import org.depaul.logic.bricks.Brick;
import org.depaul.logic.bricks.BrickGenerator;
import org.depaul.logic.bricks.RandomBrickGenerator;
import org.depaul.logic.data.Score;
import org.depaul.logic.data.ViewData;
import org.depaul.logic.rotator.BrickRotator;
import org.depaul.logic.util.Operations;

import java.awt.*;
import org.depaul.logic.bricks.Brick;
import org.depaul.logic.bricks.IBrick;
import org.depaul.logic.bricks.LBrick;
// Once these bricks have been implemented, uncomment the following lines
//import org.depaul.logic.bricks.JBrick;
import org.depaul.logic.bricks.TBrick;
import org.depaul.logic.bricks.SBrick;
//import org.depaul.logic.bricks.ZBrick;
//import org.depaul.logic.bricks.OBrick;
import org.depaul.logic.bricks.UniqueBrick;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private int currentColorId = 0;
    private int nextColorId = 1;

    private static final int ID_I = 1;  // cyan
    private static final int ID_O = 2;  // yellow
    private static final int ID_T = 3;  // purple
    private static final int ID_L = 4;  // orange
    private static final int ID_J = 5;  // blue
    private static final int ID_S = 6;  // green
    private static final int ID_Z = 7;  // red
    private static final int ID_Unique = 8; // brown

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean createNewBrick() {
        // Generates the next random brick from the brick generator
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);

        // Fixed colors by piece type
        currentColorId = colorFor(currentBrick);
        nextColorId = colorFor(brickGenerator.getNextBrick());

        // Sets the brick's position top of board
        currentOffset = new Point(3, 0);

        // Check if the new brick overlaps existing blocks, if true, means full which is game over
        return Operations.intersectMatrix(
                currentGameMatrix,
                tint(brickRotator.getCurrentShapeMatrix(), currentColorId),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        // Current active piece and tints it with active color
        int[][] active = tint(brickRotator.getCurrentShapeMatrix(), currentColorId);

        // Next preview, for first rotation of upcoming brick
        int[][] nextShape = brickGenerator.getNextBrick().getBrickMatrixList().get(0);
        int[][] nextTinted = tint(nextShape, nextColorId);

        // Returns snapshot for the GUI to render
        return new ViewData(
                active,
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextTinted
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = Operations.mergeMatrix(
                currentGameMatrix,
                tint(brickRotator.getCurrentShapeMatrix(), currentColorId),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }

    public boolean tryMoveLeft()  {
        // Moves brick to left
        return tryMoveBy(-1, 0);
    }

    public boolean tryMoveRight() {
        // Moves brick to right
        return tryMoveBy( 1, 0);
    }

    public boolean tryMoveDown()  {
        // Moves brick down
        return tryMoveBy( 0, 1);
    }

    private boolean tryMoveBy(int dx, int dy) {
        int[][] shape = brickRotator.getCurrentShapeMatrix();

        // New coordinates which will be compared to test collisions
        int nx = (int) currentOffset.getX() + dx;
        int ny = (int) currentOffset.getY() + dy;

        // If there are any collision, with the board edges, background, it cancels the move
        if (Operations.intersectMatrix(currentGameMatrix, shape, nx, ny)) {
            return false;
        }

        // Means no collisions, movement is allowed
        currentOffset.translate(dx, dy);
        return true;
    }

    public boolean rotateCW()  {
        return tryRotate(brickRotator.getNextShapeMatrixIndexCW());
    }

    public boolean rotateCCW() {
        return tryRotate(brickRotator.getNextShapeMatrixIndexCCW());
    }

    private boolean tryRotate(int nextIndex) {
        int[][] nextShape = brickRotator.getShapeMatrix(nextIndex);
        if (Operations.intersectMatrix(currentGameMatrix, nextShape, (int) currentOffset.getX(), (int) currentOffset.getY())) {
            return false;
        }
        brickRotator.setCurrentShapeIndex(nextIndex);
        return true;
    }

    public int hardDrop() {
        int rows = 0;
        while (tryMoveDown()) rows++;
        return rows;
    }

    @Override
    public int clearFullLines() {
        int rows = currentGameMatrix.length;
        int cols = currentGameMatrix[0].length;
        int cleared = 0;

        for (int r = rows - 1; r >= 0; r--) {
            if (isRowFull(r, cols)) {
                removeRow(r, cols);
                cleared++;
                r++; // recheck same row after shift
            }
        }
        return cleared;
    }

    private boolean isRowFull(int r, int cols) {
        for (int c = 0; c < cols; c++) {
            if (currentGameMatrix[r][c] == 0) {
                return false;
            }
        }
        return true;
    }

    private void removeRow(int r, int cols) {
        for (int rr = r; rr > 0; rr--) {
            for (int c = 0; c < cols; c++) {
                currentGameMatrix[rr][c] = currentGameMatrix[rr - 1][c];
            }
        }
        for (int c = 0; c < cols; c++) currentGameMatrix[0][c] = 0;
    }

    private int[][] tint(int[][] shape, int colorId) {
        // Create a new matric of same dimensions to hold tinted version
        int[][] out = new int[shape.length][shape[0].length];
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                // If the cell contains part of the brick assign its color, otherwise, leaves it transparent
                out[r][c] = (shape[r][c] == 0) ? 0 : colorId;
            }
        }
        return out;
    }

    private int colorFor(Brick b) {
        if (b instanceof IBrick) return ID_I;
        if (b instanceof LBrick) return ID_L;
        // Uncomment the following once bricks have been built.
//        if (b instanceof OBrick) return ID_O;
        if (b instanceof TBrick) return ID_T;
//        if (b instanceof JBrick) return ID_J;
        if (b instanceof SBrick) return ID_S;
//        if (b instanceof ZBrick) return ID_Z;
        if (b instanceof UniqueBrick) return ID_Unique;
        return ID_Z; // fallback (shouldn't hit)
    }
}