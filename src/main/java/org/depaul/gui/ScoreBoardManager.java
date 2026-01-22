package org.depaul.gui;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import org.depaul.logic.data.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreBoardManager {

    //create a list to hold the last scores, once game over store score somewhere
    public ArrayList<Score> scores = new ArrayList<Score>();

    public ScoreBoardManager(ArrayList<Score> scores) {
        this.scores = scores;
    }

    public void addScore(Score lastScore) {
        scores.add(lastScore);
        sortScores();
    }

    public ArrayList<Score> getTopScores() {
        int end = Math.min(10, scores.size());
        return new ArrayList<>(scores.subList(0, end));
    }

    public void sortScores() {
        scores.sort(Comparator.comparing(Score::scoreProperty,
                Comparator.comparing(IntegerProperty::getValue)).reversed());
    }

}