package snake;

import javafx.scene.Scene;

/**
 * Wraps SnakeGame so GameManager can launch it cleanly.
 */
public class SnakeController {

    private SnakeGame snakeGame;

    public SnakeController() {
        snakeGame = new SnakeGame();
    }

    public Scene getGameScene() {
        return snakeGame.createGameScene();
    }
}