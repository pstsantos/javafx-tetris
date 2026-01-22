package org.depaul.logic.bricks;

import org.junit.Assert;
import org.junit.Test;

public class TBrickTest {

    @Test
    public void testGetShapeMatrix() {
        Brick brick = new TBrick();
        brick.getBrickMatrixList().get(0)[0][0] = 0;
        brick.getBrickMatrixList().get(0)[1][1] = 2;
        Assert.assertEquals(0, brick.getBrickMatrixList().get(0)[0][0]);
        Assert.assertEquals(2, brick.getBrickMatrixList().get(0)[1][1]);
    }

    @Test
    public void testGetShapeMatrixList() {
        Brick brick = new TBrick();
        brick.getBrickMatrixList().remove(0);
        Assert.assertEquals(4, brick.getBrickMatrixList().size());
    }
}