package org.depaul.logic.bricks;

import org.junit.Assert;
import org.junit.Test;

public class SBrickTest {

    @Test
    public void testGetShapeMatrix() {
        Brick brick = new SBrick();
        brick.getBrickMatrixList().get(0)[1][2] = 9;
        brick.getBrickMatrixList().get(0)[2][1] = 7;
        Assert.assertEquals(6, brick.getBrickMatrixList().get(0)[1][2]);
        Assert.assertEquals(6, brick.getBrickMatrixList().get(0)[2][1]);
    }

    @Test
    public void testGetShapeMatrixList() {
        Brick brick = new SBrick();
        brick.getBrickMatrixList().remove(0);
        Assert.assertEquals(4, brick.getBrickMatrixList().size());
    }
}
