package org.depaul.logic.bricks;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class UniqueBrickTest extends TestCase {

    @Test
    public void testGetShapeMatrix() {
        Brick brick = new UniqueBrick();

        brick.getBrickMatrixList().get(0)[0][0] = 9;
        brick.getBrickMatrixList().get(0)[1][2] = 5;

        Assert.assertEquals(0, brick.getBrickMatrixList().get(0)[0][0]);
        Assert.assertEquals(8, brick.getBrickMatrixList().get(0)[1][2]);
    }

    @Test
    public void testGetShapeMatrixList() {
        Brick brick = new UniqueBrick();

        brick.getBrickMatrixList().remove(0);
        Assert.assertEquals(4, brick.getBrickMatrixList().size());
    }

    @Test
    public void testGetBrickMatrixFourRotations() {
        Brick brick = new UniqueBrick();

        Assert.assertEquals(4, brick.getBrickMatrixList().size());
    }
}
