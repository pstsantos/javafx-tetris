package org.depaul.logic.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty totalLinesCleared = new SimpleIntegerProperty(0);
    
    // Official Tetris scoring constants
    private static final int SINGLE_LINE_BASE = 40;
    private static final int DOUBLE_LINE_BASE = 100;
    private static final int TRIPLE_LINE_BASE = 300;
    private static final int TETRIS_LINE_BASE = 1200;
    private static final int LINES_PER_LEVEL = 10;

    public IntegerProperty scoreProperty() {
        return score;
    }
    
    public IntegerProperty levelProperty() {
        return level;
    }
    
    public IntegerProperty totalLinesClearedProperty() {
        return totalLinesCleared;
    }
    
    public int getLevel() {
        return level.getValue();
    }
    
    public int getTotalLinesCleared() {
        return totalLinesCleared.getValue();
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
    }
    
    /**
     * Adds points based on official Tetris scoring system for line clears
     * @param linesCleared Number of lines cleared (1-4)
     */
    public void addLinesClearScore(int linesCleared) {
        if (linesCleared <= 0 || linesCleared > 4) {
            return; // Invalid input
        }
        
        int basePoints = switch (linesCleared) {
            case 1 -> SINGLE_LINE_BASE;    // Single: 40 × (level + 1)
            case 2 -> DOUBLE_LINE_BASE;    // Double: 100 × (level + 1)
            case 3 -> TRIPLE_LINE_BASE;    // Triple: 300 × (level + 1)
            case 4 -> TETRIS_LINE_BASE;    // Tetris: 1200 × (level + 1)
            default -> 0;
        };
        
        int points = basePoints * (level.getValue() + 1);
        score.set(score.get() + points);
        
        // Update total lines cleared and check for level progression
        totalLinesCleared.set(totalLinesCleared.get() + linesCleared);
        int newLevel = (totalLinesCleared.get() / LINES_PER_LEVEL) + 1;
        if (newLevel != level.get()) level.set(newLevel);
    }
    
//    /**
//     * Updates level based on total lines cleared (every 10 lines = level up)
//     */
//    private void updateLevel() {
//        int newLevel = (totalLinesCleared.getValue() / LINES_PER_LEVEL) + 1;
//        if (newLevel > level.getValue()) {
//            level.setValue(newLevel);
//        }
//    }
    
    /**
     * Gets lines needed to reach next level
     * @return Number of lines needed for next level
     */
    public int getLinesUntilNextLevel() {
        int nextLevelLines = level.getValue() * LINES_PER_LEVEL;
        return nextLevelLines - totalLinesCleared.getValue();
    }

    public void reset() {
        score.setValue(0);
        level.setValue(1);
        totalLinesCleared.setValue(0);
    }
}
