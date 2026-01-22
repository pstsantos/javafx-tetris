package org.depaul.gui;

import junit.framework.TestCase;
import org.depaul.logic.data.Score;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

public class ScoreBoardManagerTest extends TestCase {

    private ScoreBoardManager manager;
    public void testAddScore() {
        manager = new ScoreBoardManager(new ArrayList<>());
        // Add test data
        for (int value : new int[]{50, 120, 75, 300, 90, 40, 250, 200, 180, 100, 310, 10}) {
            Score s = new Score();
            s.scoreProperty().setValue(value);
            manager.addScore(s);
        }

        // Check that the first element (index 0) has the highest score
        assertEquals(310, manager.scores.get(0).scoreProperty().get());
        // Check that the last element has the lowest
        assertEquals(10, manager.scores.get(manager.scores.size() - 1).scoreProperty().get());
    }

    public void testGetTopScores() {
        manager = new ScoreBoardManager(new ArrayList<>());
        for (int value : new int[]{50, 120, 75, 300, 90, 40, 250, 200, 180, 100, 310, 10}) {
            Score s = new Score();
            s.scoreProperty().setValue(value);
            manager.addScore(s);
        }

        List<Score> top10 = manager.getTopScores();
        // Should not exceed 10
        assertEquals(10, top10.size());
        // Should start with the highest
        assertEquals(310, top10.get(0).scoreProperty().get());
    }


    @Test
    public void testGetTopScoresWhenFewerThan10() {
        manager = new ScoreBoardManager(new ArrayList<>());
        for (int value : new int[]{50, 120, 75}) {
            Score s = new Score();
            s.scoreProperty().setValue(value);
            manager.addScore(s);
        }

        List<Score> top = manager.getTopScores();
        // Should not crash and should return 3
        assertEquals(3, top.size());
        // Should be sorted descending
        assertEquals(120, top.get(0).scoreProperty().get());
        assertEquals(50, top.get(2).scoreProperty().get());
    }
}