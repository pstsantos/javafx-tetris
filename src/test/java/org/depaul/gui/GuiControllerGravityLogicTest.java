package org.depaul.gui;

import org.junit.Test;
import org.junit.Assert;

public class GuiControllerGravityLogicTest {

    private int calculateGravityDelay(int level) {
        return Math.max(100, 500 - ((level - 1) * 40));
    }

    @Test
    public void testInitialGravityDelay() {
        Assert.assertEquals(500, calculateGravityDelay(1));
    }

    @Test
    public void testMinGravityDelay() {
        Assert.assertEquals(100, calculateGravityDelay(11));
        Assert.assertEquals(100, calculateGravityDelay(20));
    }

    @Test
    public void testDecreaseGravityDelay() {
        Assert.assertEquals(300, calculateGravityDelay(6));
    }

}