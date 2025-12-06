package snake;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import manager.HighScoreController;

public class SnakeGame {

    private static final int STARTING_SCORE = 1000;
    
    private Snake snake;
    private Food food;
    private GameBoard board;
    private boolean paused = false;
    private AnimationTimer timer;
    private int score = STARTING_SCORE;
    private Text scoreText;
    private StackPane gameRoot;
    private javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> keyEventHandler;

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

        scoreText = new Text("Score: " + STARTING_SCORE);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font(20));

        StackPane root = new StackPane();
        root.getChildren().addAll(board.getCanvas(), scoreText);
        StackPane.setAlignment(scoreText, Pos.TOP_CENTER);
        root.setFocusTraversable(true);
        
        // Store reference to root for restart functionality
        this.gameRoot = root;
        
        Scene scene = new Scene(root);
        
        // Store reference to root for pause functionality
        final StackPane gameRootPane = root;

        // Create and store key event handler so it can be reattached after restart
        keyEventHandler = e -> {
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
        };
        
        scene.setOnKeyPressed(keyEventHandler);
        root.setOnKeyPressed(keyEventHandler);

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
        // Save high score
        String username = manager.GameManager.getCurrentUser();
        if (username != null && !username.isEmpty() && score > 0) {
            HighScoreController.updateScore(username, score, "snake");
        }
        
        // Create game over overlay with score and restart button
        VBox gameOverBox = new VBox(20);
        gameOverBox.setAlignment(Pos.CENTER);
        
        Text over = new Text("GAME OVER");
        over.setFont(Font.font(40));
        over.setFill(Color.RED);
        
        Text scoreDisplay = new Text("Final Score: " + score);
        scoreDisplay.setFont(Font.font(24));
        scoreDisplay.setFill(Color.WHITE);
        
        Button restartButton = new Button("Restart Game");
        restartButton.setFont(Font.font(18));
        restartButton.setStyle("-fx-padding: 10 20 10 20;");
        restartButton.setOnAction(e -> restartGame(root));
        
        gameOverBox.getChildren().addAll(over, scoreDisplay, restartButton);
        root.getChildren().add(gameOverBox);
        StackPane.setAlignment(gameOverBox, Pos.CENTER);
    }

    private void restartGame(StackPane root) {
        // Remove game over overlay and any pause overlays
        root.getChildren().removeIf(node -> node instanceof VBox || node instanceof PauseOverlay);
        
        // Stop the old timer
        if (timer != null) {
            timer.stop();
        }
        
        // Reset game state
        snake = new Snake(
                SnakeConstants.BOARD_COLS / 2,
                SnakeConstants.BOARD_ROWS / 2
        );
        score = STARTING_SCORE;
        scoreText.setText("Score: " + STARTING_SCORE);
        paused = false;
        
        // Reset food - ensure it doesn't spawn on snake
        do {
            food.spawn(board.getCols(), board.getRows());
        } while (isFoodOnSnake());
        
        // Remove any existing key handlers first to avoid conflicts
        javafx.scene.Scene scene = root.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(null);
            javafx.scene.Parent sceneRoot = scene.getRoot();
            if (sceneRoot != null) {
                sceneRoot.setOnKeyPressed(null);
            }
        }
        root.setOnKeyPressed(null);
        
        // Recreate key handler to reference the new snake instance
        final StackPane gameRootPane = root;
        keyEventHandler = e -> {
            Snake.Direction newDir = null;
            switch (e.getCode()) {
                case UP -> newDir = Snake.Direction.UP;
                case DOWN -> newDir = Snake.Direction.DOWN;
                case LEFT -> newDir = Snake.Direction.LEFT;
                case RIGHT -> newDir = Snake.Direction.RIGHT;
                case ESCAPE -> togglePause(gameRootPane);
            }
            if (newDir != null) {
                if (!snake.isOpposite(newDir) && !wouldCauseImmediateCollision(newDir)) {
                    snake.setDirection(newDir);
                }
            }
        };
        
        // Attach new handler to all necessary nodes
        if (scene != null) {
            scene.setOnKeyPressed(keyEventHandler);
            javafx.scene.Parent sceneRoot = scene.getRoot();
            if (sceneRoot != null) {
                sceneRoot.setOnKeyPressed(keyEventHandler);
                sceneRoot.setFocusTraversable(true);
            }
        }
        root.setOnKeyPressed(keyEventHandler);
        root.setFocusTraversable(true);
        
        // Create and start new game timer
        timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (paused) return;
                if (now - lastUpdate < SnakeConstants.FRAME_DELAY) return;

                snake.move();
                
                SnakeSegment head = snake.getSegments().get(0);
                boolean foodEaten = (head.getX() == food.getX() && head.getY() == food.getY());
                
                if (foodEaten) {
                    score++;
                    scoreText.setText("Score: " + score);
                    snake.grow();
                    do {
                        food.spawn(board.getCols(), board.getRows());
                    } while (isFoodOnSnake());
                }
                
                if (checkGameOver(foodEaten)) {
                    stop();
                    showGameOverOverlay(root);
                }

                board.render(snake, food);
                lastUpdate = now;
            }
        };
        timer.start();
        
        // Request focus multiple times to ensure it sticks
        javafx.application.Platform.runLater(() -> {
            javafx.scene.Scene focusScene = root.getScene();
            if (focusScene != null) {
                javafx.scene.Parent sceneRoot = focusScene.getRoot();
                if (sceneRoot != null) {
                    sceneRoot.setFocusTraversable(true);
                    sceneRoot.requestFocus();
                    // Also try requesting focus on root as backup
                    root.setFocusTraversable(true);
                    root.requestFocus();
                } else {
                    root.requestFocus();
                }
            } else {
                root.requestFocus();
            }
        });
        
        // Additional focus request after a short delay to ensure it works
        javafx.application.Platform.runLater(() -> {
            javafx.scene.Scene focusScene = root.getScene();
            if (focusScene != null) {
                javafx.scene.Parent sceneRoot = focusScene.getRoot();
                if (sceneRoot != null) {
                    sceneRoot.requestFocus();
                }
            }
        });
    }

    public void resetGame() {
        // Reset snake
        snake = new Snake(
                SnakeConstants.BOARD_COLS / 2,
                SnakeConstants.BOARD_ROWS / 2
        );
        // Reset score
        score = STARTING_SCORE;
        scoreText.setText("Score: " + STARTING_SCORE);
        // Reset food - ensure it doesn't spawn on snake
        do {
            food.spawn(board.getCols(), board.getRows());
        } while (isFoodOnSnake());
        // Reset pause state
        paused = false;
        // Re-render the board
        if (board != null) {
            board.render(snake, food);
        }
    }
}