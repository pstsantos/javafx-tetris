package org.depaul.logic.rotator;

import org.depaul.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShapeIndex = 0;
    private int nextShapeIndex;

    public int[][] getNextShapeMatrix() {
        nextShapeIndex = currentShapeIndex;
        nextShapeIndex = (++nextShapeIndex) % brick.getBrickMatrixList().size();
        return brick.getBrickMatrixList().get(nextShapeIndex);
    }

    public int[][] getCurrentShapeMatrix() {
        return brick.getBrickMatrixList().get(currentShapeIndex);
    }

    public int getCurrentShapeIndex() {
        return currentShapeIndex;
    }

    public int getNextShapeIndex() {
        return nextShapeIndex;
    }

    public void setCurrentShapeIndex(int currentShapeIndex) {
        this.currentShapeIndex = currentShapeIndex;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShapeIndex = 0;
    }

    public int getNextShapeMatrixIndexCW() {
        // Clockwise = +1 (wrap around)
        int next = currentShapeIndex + 1;
        if (next >= brick.getBrickMatrixList().size()) {
            next = 0;   // wraps back to first rotation
        }
        return next;
    }

    public int getNextShapeMatrixIndexCCW() {
        // Counter-clockwise = -1 (wrap around)
        int next = currentShapeIndex - 1;
        if (next < 0) {
            next = brick.getBrickMatrixList().size() - 1;   // wraps to last rotation
        }
        return next;
    }

    public int[][] getShapeMatrix(int index) {
        // Returns the shape matrix for the specified rotation index
        return brick.getBrickMatrixList().get(index);
    }
}
