package org.depaul.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.depaul.logic.data.Score;
import java.util.List;

public class GameOverPanel extends VBox {
    private final VBox leaderboardBox;
    private final Label title;

    public GameOverPanel() { this("Game Over!"); }

    public GameOverPanel(String text) {
        setAlignment(Pos.CENTER);
        setSpacing(16);
        setFillWidth(true);
        setStyle("-fx-background-color: rgba(0,0,0,0.82);"
                + "-fx-padding: 24; -fx-background-radius: 14;");

        title = new Label(text);
        title.setTextOverrun(OverrunStyle.CLIP);
        title.setWrapText(false);
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: rgba(241,0,0,0.9);");
        title.setMinWidth(Region.USE_PREF_SIZE);

        leaderboardBox = new VBox(8);
        leaderboardBox.setAlignment(Pos.CENTER);

        getChildren().addAll(title, leaderboardBox);
    }

    public void showLeaderboard(List<Score> topScores) {
        leaderboardBox.getChildren().clear();

        Label hdr = new Label("🏆 Top Scores");
        hdr.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: gold;");
        leaderboardBox.getChildren().add(hdr);

        int rank = 1;
        for (Score s : topScores) {
            Label row = new Label(rank + ". " + s.scoreProperty().get());
            row.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            leaderboardBox.getChildren().add(row);
            rank++;
        }
    }
}