package org.depaul.data;
import org.depaul.logic.data.Score;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class scoreTest {

    private Score score;

    @Before
    public void setUp() {
        score = new Score();
        score.levelProperty().set(1); // Start at level 0
        score.scoreProperty().set(0);
        score.totalLinesClearedProperty().set(0);
    }

    @Test
    public void testAddScore() {
        Score score = new Score();
        score.add(10);
        Assert.assertEquals(10, score.scoreProperty().get());
        score.add(5);
        Assert.assertEquals(15, score.scoreProperty().get());
    }

    @Test
    public void testResetScore() {
        Score score = new Score();
        score.add(20);
        Assert.assertEquals(20, score.scoreProperty().get());
        score.reset();
        Assert.assertEquals(0, score.scoreProperty().get());
    }

    @Test
    public void testInitialScore() {
        Score score = new Score();
        Assert.assertEquals(0, score.scoreProperty().get());
    }

    @Test public void testMultipleAdds() {
        Score score = new Score();
        for (int i = 1; i <= 5; i++) {
            score.add(i * 10); // Add 10, 20, 30, 40, 50
        }
        Assert.assertEquals(150, score.scoreProperty().get());
    }

    @Test
    public void testSingleLineScoreAdded() {
        Score score = new Score();
        score.addLinesClearScore(1);
        // Level = 1 → multiplier = 2 → 40 * 2 = 80
        Assert.assertEquals(80, score.scoreProperty().get());
    }

    @Test
    public void testDoubleLineScoreAdded() {
        Score score = new Score();
        score.addLinesClearScore(2);
        Assert.assertEquals(200, score.scoreProperty().get());
    }

    @Test
    public void testTripleLineScoreAdded() {
        Score score = new Score();
        score.addLinesClearScore(3);
        Assert.assertEquals(600, score.scoreProperty().get());
    }

    @Test
    public void testTetrisLineScoreAdded() {
        Score score = new Score();
        score.addLinesClearScore(4);
        Assert.assertEquals(2400, score.scoreProperty().get());
    }

    @Test
    public void testInvalidLinesClearedZero() {
        score.addLinesClearScore(0);
        Assert.assertEquals(0, score.scoreProperty().get());
        Assert.assertEquals(0, score.totalLinesClearedProperty().get());
    }

    @Test
    public void testInvalidLinesClearedNegative() {
        score.addLinesClearScore(-3);
        Assert.assertEquals(0, score.scoreProperty().get());
        Assert.assertEquals(0, score.totalLinesClearedProperty().get());
    }

    @Test
    public void testInvalidLinesClearedAboveFour() {
        score.addLinesClearScore(5);
        Assert.assertEquals(0, score.scoreProperty().get());
        Assert.assertEquals(0, score.totalLinesClearedProperty().get());
    }

    @Test
    public void testLevelMultiplierAffectsScore() {
        score.levelProperty().set(5);
        score.addLinesClearScore(2);
        int expected = 100 * 6;
        Assert.assertEquals(expected, score.scoreProperty().get());
    }

    @Test
    public void testLevelStaysSameUnderTenLines() {
        Score score = new Score();
        // Clear 9 lines in total (1+2+3+3)
        score.addLinesClearScore(1);
        score.addLinesClearScore(2);
        score.addLinesClearScore(3);
        score.addLinesClearScore(3);
        Assert.assertEquals(1, score.levelProperty().get());
        Assert.assertEquals(9, score.totalLinesClearedProperty().get());
    }

    @Test
    public void testLevelIncreasesAfterTenLines() {
        Score score = new Score();

        score.addLinesClearScore(4);
        score.addLinesClearScore(4);
        score.addLinesClearScore(2);
        Assert.assertEquals(2, score.levelProperty().get());
        Assert.assertEquals(10, score.totalLinesClearedProperty().get());
    }

    @Test
    public void testLevelIncreasesMultipleTimes() {
        Score score = new Score();

        for (int i = 0; i < 6; i++) {
            score.addLinesClearScore(4);
        }
        score.addLinesClearScore(1);
        Assert.assertEquals(3, score.levelProperty().get());
        Assert.assertEquals(25, score.totalLinesClearedProperty().get());
    }

    @Test
    public void testLinesUntilNextLevelCalculation() {
        Score score = new Score();
        score.addLinesClearScore(5);
        Assert.assertEquals(10, score.getLinesUntilNextLevel());

        score.addLinesClearScore(4);
        Assert.assertEquals(6, score.getLinesUntilNextLevel());
    }

    @Test
    public void testLevelDoesNotDecrease() {
        Score score = new Score();
        for (int i = 0; i < 10; i++) {
            score.addLinesClearScore(1);
        }
        Assert.assertEquals(2, score.levelProperty().get());

        score.reset(); // Reset to level 1
        Assert.assertEquals(1, score.levelProperty().get());
    }
    @Test
    public void testLinesUntilNextLevelAtStart() {

        Assert.assertEquals(10, score.getLinesUntilNextLevel());
    }

    @Test
    public void testLinesUntilNextLevelMidLevel() {
        score.totalLinesClearedProperty().set(3);
        Assert.assertEquals(7, score.getLinesUntilNextLevel());
    }

    @Test
    public void testLinesUntilNextLevelAfterLevelUp() {
        score.levelProperty().set(2);
        score.totalLinesClearedProperty().set(12);

        Assert.assertEquals(8, score.getLinesUntilNextLevel());
    }

    @Test
    public void testLinesUntilNextLevelExactThreshold() {
        score.levelProperty().set(2);
        score.totalLinesClearedProperty().set(20);

        Assert.assertEquals(0, score.getLinesUntilNextLevel());
    }

    @Test
    public void testTotalLinesClearedProperty() {

        Assert.assertEquals(0, score.totalLinesClearedProperty().get());


        score.totalLinesClearedProperty().set(5);
        Assert.assertEquals(5, score.getTotalLinesCleared());

        score.totalLinesClearedProperty().set(score.totalLinesClearedProperty().get() + 2);
        Assert.assertEquals(7, score.getTotalLinesCleared());
    }

    @Test
    public void testGetTotalLinesCleared() {
        Assert.assertEquals(0, score.getTotalLinesCleared());

        score.totalLinesClearedProperty().set(5);
        Assert.assertEquals(5, score.getTotalLinesCleared());

        score.totalLinesClearedProperty().set(12);
        Assert.assertEquals(12, score.getTotalLinesCleared());
    }

}