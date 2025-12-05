package blackjack;
import javafx.scene.Scene;

public class BlackjackController {

    private final BlackjackGame blackjackGame;

    public BlackjackController(String username) {
        this.blackjackGame = new BlackjackGame(username);
    }

    public Scene getMainMenuScene() {
        return blackjackGame.createMainMenuScene();
    }

    public Scene getGameScene() {
        return blackjackGame.createGameScene();
    }

    public BlackjackGame getBlackjackGame() {
        return blackjackGame;
    }
}
