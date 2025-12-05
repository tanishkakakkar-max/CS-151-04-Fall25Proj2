package snake;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class SnakeGame {

    private Snake snake;
    private Food food;
    private GameBoard board;
    private boolean paused = false;
    private AnimationTimer timer;
    private int score = 0;

    public Scene createGameScene() {

        board = new GameBoard(25, 25, 20);
        snake = new Snake(12, 12);
        food = new Food(board.getCols(), board.getRows());

        StackPane root = new StackPane();
        root.getChildren().add(board.getCanvas());

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> snake.setDirection(Snake.Direction.UP);
                case DOWN -> snake.setDirection(Snake.Direction.DOWN);
                case LEFT -> snake.setDirection(Snake.Direction.LEFT);
                case RIGHT -> snake.setDirection(Snake.Direction.RIGHT);
                case ESCAPE -> togglePause(root);
            }
        });

        timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (paused) return;
                if (now - lastUpdate < 150_000_000) return; // Slow down snake

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
            snake.grow();
            food.spawn(board.getCols(), board.getRows());
        }
    }
    private boolean checkGameOver() {
        SnakeSegment head = snake.getSegments().get(0);

        if (head.getX() < 0 || head.getX() >= board.getCols()) return true;
        if (head.getY() < 0 || head.getY() >= board.getRows()) return true;

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