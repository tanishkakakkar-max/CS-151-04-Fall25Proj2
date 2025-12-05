package snake;

import javafx.scene.Scene;
public class SnakeController {

    private SnakeGame snakeGame;

    public SnakeController() {
        snakeGame = new SnakeGame();
    }

    public Scene getGameScene() {
        return snakeGame.createGameScene();
    }
}