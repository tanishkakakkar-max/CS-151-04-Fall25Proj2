package snake;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import manager.HighScoreController;

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
        
        // Store reference to root for pause functionality
        final StackPane gameRootPane = root;

        scene.setOnKeyPressed(e -> {
            Snake.Direction newDir = null;
            switch (e.getCode()) {
                case UP -> newDir = Snake.Direction.UP;
                case DOWN -> newDir = Snake.Direction.DOWN;
                case LEFT -> newDir = Snake.Direction.LEFT;
                case RIGHT -> newDir = Snake.Direction.RIGHT;
                case ESCAPE -> togglePause(gameRootPane);
            }
            if (newDir != null) {
                // Prevent opposite direction and also check if direction would cause immediate collision
                if (!snake.isOpposite(newDir) && !wouldCauseImmediateCollision(newDir)) {
                    snake.setDirection(newDir);
                }
            }
        });

        timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (paused) return;
                if (now - lastUpdate < SnakeConstants.FRAME_DELAY) return; // Slow down snake

                snake.move();
                
                // Check food collision after move
                SnakeSegment head = snake.getSegments().get(0);
                boolean foodEaten = (head.getX() == food.getX() && head.getY() == food.getY());
                
                if (foodEaten) {
                    score++;
                    scoreText.setText("Score: " + score);
                    // Grow the snake - this adds a segment at the current tail position
                    snake.grow();
                    // Spawn food at a location not occupied by the snake
                    do {
                        food.spawn(board.getCols(), board.getRows());
                    } while (isFoodOnSnake());
                }
                
                // Check game over - but skip self-collision check if we just ate food
                // because the new segment is at the tail position which is safe
                if (checkGameOver(foodEaten)) {
                    stop();
                    showGameOverOverlay(root);
                }

                board.render(snake, food);
                lastUpdate = now;
            }
        };

        timer.start();
        return scene;
    }
    private boolean isFoodOnSnake() {
        for (SnakeSegment seg : snake.getSegments()) {
            if (seg.getX() == food.getX() && seg.getY() == food.getY()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean wouldCauseImmediateCollision(Snake.Direction newDir) {
        // If snake has only 1 segment, any direction is safe
        if (snake.getSegments().size() <= 1) {
            return false;
        }
        
        // Calculate where the head would be if we move in the new direction
        SnakeSegment head = snake.getSegments().get(0);
        int nextX = head.getX();
        int nextY = head.getY();
        
        switch (newDir) {
            case UP -> nextY--;
            case DOWN -> nextY++;
            case LEFT -> nextX--;
            case RIGHT -> nextX++;
        }
        
        // Check if the next position would collide with the second segment (the neck)
        // This prevents the snake from turning back into itself
        if (snake.getSegments().size() > 1) {
            SnakeSegment neck = snake.getSegments().get(1);
            if (nextX == neck.getX() && nextY == neck.getY()) {
                return true;
            }
        }
        
        return false;
    }
    private boolean checkGameOver(boolean justAteFood) {
        SnakeSegment head = snake.getSegments().get(0);

        // Always check wall collision
        if (!board.isInside(head.getX(), head.getY())) {
            playWallHitSound();
            return true;
        }

        // Skip self-collision check if we just ate food, because the new segment
        // is at the tail position which the head just left, so it's safe
        if (!justAteFood) {
            for (int i = 1; i < snake.getSegments().size(); i++) {
                SnakeSegment seg = snake.getSegments().get(i);
                if (seg.getX() == head.getX() && seg.getY() == head.getY()) {
                    return true;
                }
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

    private void showGameOverOverlay(StackPane root) {
        Text over = new Text("GAME OVER");
        over.setFont(Font.font(40));
        over.setFill(Color.RED);
        root.getChildren().add(over);
        StackPane.setAlignment(over, Pos.CENTER);
        
        // Save high score
        String username = manager.GameManager.getCurrentUser();
        if (username != null && !username.isEmpty() && score > 0) {
            HighScoreController.updateScore(username, score, "snake");
        }
    }

    public void resetGame() {
        snake = new Snake(
                SnakeConstants.BOARD_COLS / 2,
                SnakeConstants.BOARD_ROWS / 2
        );
        score = 0;
        scoreText.setText("Score: 0");
        food.spawn(board.getCols(), board.getRows());
    }
}