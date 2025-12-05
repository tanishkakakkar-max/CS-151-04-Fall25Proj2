package snake;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SnakeGame {

    private Snake snake;
    private Food food;
    private GameBoard board;
    private boolean paused = false;
    private AnimationTimer timer;
    private int score = 0;
    private Text scoreText;

    // Placeholder for optional sound effects
    private void playWallHitSound() {
        // future: play audio clip
    }

    public Scene createGameScene() {

        board = new GameBoard(
            SnakeConstants.BOARD_ROWS,
            SnakeConstants.BOARD_COLS,
            SnakeConstants.CELL_SIZE
        );
        snake = new Snake(
            SnakeConstants.BOARD_COLS / 2,
            SnakeConstants.BOARD_ROWS / 2
        );
        food = new Food(board.getCols(), board.getRows());

        scoreText = new Text("Score: 0");
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font(20));

        StackPane root = new StackPane();
        root.getChildren().addAll(board.getCanvas(), scoreText);
        StackPane.setAlignment(scoreText, Pos.TOP_CENTER);

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            Snake.Direction newDir = null;
            switch (e.getCode()) {
                case UP -> newDir = Snake.Direction.UP;
                case DOWN -> newDir = Snake.Direction.DOWN;
                case LEFT -> newDir = Snake.Direction.LEFT;
                case RIGHT -> newDir = Snake.Direction.RIGHT;
                case ESCAPE -> togglePause(root);
            }
            if (newDir != null && !snake.isOpposite(newDir)) {
                snake.setDirection(newDir);
            }
        });

        timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (paused) return;
                if (now - lastUpdate < SnakeConstants.FRAME_DELAY) return; // Slow down snake

                snake.move();
                checkFoodCollision();
                if (checkGameOver()) {
                    stop();
                }

                board.render(snake, food);
                lastUpdate = now;
            }
        };

        timer.start();
        return scene;
    }
    private void checkFoodCollision() {
        SnakeSegment head = snake.getSegments().get(0);

        if (head.getX() == food.getX() && head.getY() == food.getY()) {
            score++;
            scoreText.setText("Score: " + score);
            snake.grow();
            food.spawn(board.getCols(), board.getRows());
        }
    }
    private boolean checkGameOver() {
        SnakeSegment head = snake.getSegments().get(0);

        if (head.getX() < 0 || head.getX() >= board.getCols()) {
            playWallHitSound();
            return true;
        }
        if (head.getY() < 0 || head.getY() >= board.getRows()) {
            playWallHitSound();
            return true;
        }

        for (int i = 1; i < snake.getSegments().size(); i++) {
            SnakeSegment seg = snake.getSegments().get(i);
            if (seg.getX() == head.getX() && seg.getY() == head.getY()) {
                return true;
            }
        }

        return false;
    }
    private void togglePause(StackPane root) {
        paused = !paused;

        if (paused) {
            PauseOverlay overlay = new PauseOverlay();
            root.getChildren().add(overlay);
        } else {
            root.getChildren().removeIf(node -> node instanceof PauseOverlay);
        }
    }
}