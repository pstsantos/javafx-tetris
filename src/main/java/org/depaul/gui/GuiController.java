package org.depaul.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import org.depaul.logic.data.Score;
import org.depaul.logic.data.ViewData;
import org.depaul.logic.events.EventSource;
import org.depaul.logic.events.EventType;
import org.depaul.logic.events.InputEventListener;
import org.depaul.logic.events.MoveEvent;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class GuiController implements Initializable {


    private static final int BRICK_SIZE = 20;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Text scoreValue;

    @FXML
    private Text levelValue;

    @FXML
    private Text linesValue;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane nextBrick;

    @FXML
    private GridPane brickPanel;

    @FXML
    private ToggleButton pauseButton;

    @FXML
    private StackPane gameOverNotification;

    @FXML
    private ToggleButton soundToggle;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private int gravityDelay = 500;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private MediaPlayer backgroundMusicPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("3X5.TTF").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);    // Makes main game grid receive keyboard input
        gamePanel.requestFocus();

        // Makes the "Next Brick" preview grid centered
        nextBrick.setAlignment(Pos.CENTER);
        nextBrick.setHgap(2);
        nextBrick.setVgap(2);
        nextBrick.setPadding(new Insets(4, 0, 4, 0));

//      Key bindings for game panel
        gamePanel.setOnKeyPressed(keyEvent -> {
            // Only processes keys when the game is active
            if (!isPause.getValue() && !isGameOver.getValue()) {
                switch (keyEvent.getCode()) {
                    // User wants to move left, press arrow left or A
                    case LEFT:
                    case A:
                        eventListener.onMoveEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
                        keyEvent.consume();
                        break;

                    // User wants to move right, press arrow right or D
                    case RIGHT:
                    case D:
                        eventListener.onMoveEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
                        keyEvent.consume();
                        break;

                    // User wants to move down, press arrow down or S
                    // This is a soft drop, will give +1 point to score
                    case DOWN:
                    case S:
                        eventListener.onMoveEvent(new MoveEvent(EventType.DOWN, EventSource.USER));  // soft drop
                        keyEvent.consume();
                        break;

                    // User wants to rotate clockwise, press arrow up or X
                    case UP:
                    case X:
                        eventListener.onMoveEvent(new MoveEvent(EventType.ROTATE_CW, EventSource.USER));
                        keyEvent.consume();
                        break;

                    // User wants to rotate counterclockwise, press Z
                    case Z:
                        eventListener.onMoveEvent(new MoveEvent(EventType.ROTATE_CCW, EventSource.USER));
                        keyEvent.consume();
                        break;

                    // User wants to hard drop, press space which drops piece to bottom
                    case SPACE:
                        eventListener.onMoveEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        keyEvent.consume();
                        break;

                    // User wants to restart the game, press R
                    case R:
                        newGame(null);
                        keyEvent.consume();
                        break;

                    case M:
                        if (backgroundMusicPlayer != null) {
                            backgroundMusicPlayer.setMute(!backgroundMusicPlayer.isMute());
                        }
                        keyEvent.consume();
                        break;
                }
            }
            gamePanel.requestFocus();
        });

        // GAME OVER panel notification
        gameOverNotification.setVisible(false);

        // PAUSE button
        pauseButton.selectedProperty().bindBidirectional(isPause);
        pauseButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                timeLine.pause();
                pauseButton.setText("Resume");
            } else {
                timeLine.play();
                pauseButton.setText("Pause");
            }
        });

        // SCORE: Setting the reflection style
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        scoreValue.setEffect(reflection);


        //LOAD background music
        //Works, and necessary for music within resources
        Media backgroundMusic = new Media(getClass().getResource("/MenuTheme.wav").toExternalForm());
        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);

        //Set to loop
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicPlayer.play();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        // displayMatrix is the GUI representation of the current state of the board currentGameMatrix
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // rectangles is the GUI representation of the current state of brick.
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(160+gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        generateNextBrickPanel(brick.getNextBrickData());


        timeLine = new Timeline(new KeyFrame(Duration.millis(gravityDelay),
                ae -> move(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
        Paint returnPaint = switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.web("#00FFFF");   // Cyan
            case 2 -> Color.web("#FFFF00");   // Yellow
            case 3 -> Color.web("#800080");   // Purple
            case 4 -> Color.web("#FFA500");   // Orange
            case 5 -> Color.web("#0000FF");   // Blue
            case 6 -> Color.web("#00FF00");   // Green
            case 7 -> Color.web("#FF0000");   // Red
            case 8 -> Color.web("#895129");   // Brown Cristian shape
            case 9 -> Color.web("#008080");   // Teal  David shape
            default -> Color.TRANSPARENT;
        };
        return returnPaint;
    }

    private void generateNextBrickPanel(int[][] nextBrickData) {
        nextBrick.getChildren().clear();
        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(nextBrickData[i][j], rectangle);
                if (nextBrickData[i][j] != 0) {
                    nextBrick.add(rectangle, j, i);
                }
            }
        }
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(160+gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            generateNextBrickPanel(brick.getNextBrickData());
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void move(MoveEvent event) {
        // Ensures the game is not paused
        if (isPause.getValue() == Boolean.FALSE) {
            // Sends move event to the game logic controller
            ViewData viewData = eventListener.onMoveEvent(event);
            refreshBrick(viewData);
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreValue.textProperty().bind(integerProperty.asString());
    }

    public void bindLevel(IntegerProperty integerProperty) {
        levelValue.textProperty().bind(integerProperty.asString());
    }

    public void bindLines(IntegerProperty integerProperty) {
        linesValue.textProperty().bind(integerProperty.asString());
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel = new GameOverPanel("Game Over!");
        gameOverNotification.getChildren().setAll(gameOverPanel);
        StackPane.setAlignment(gameOverPanel, Pos.CENTER);
        gameOverNotification.setVisible(true);
        isGameOver.set(true);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverNotification.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }

    @FXML
    private void toggleSound() {
        if (backgroundMusicPlayer == null) return;

        boolean muted = backgroundMusicPlayer.isMute();
        backgroundMusicPlayer.setMute(!muted);

        soundToggle.setText(muted ? "Sound: On" : "Sound: Off");
    }

    public void showLeaderboard(ArrayList<Score> topScores) {
        if (gameOverPanel != null) {
            gameOverPanel.showLeaderboard(topScores);
        }
    }

    public void setGravityDelay(int newGravityDelay) {
        gravityDelay = newGravityDelay;
        timeLine.stop();
        timeLine.getKeyFrames().setAll(new KeyFrame(Duration.millis(gravityDelay),
                ae -> move(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.play();
    }
}
