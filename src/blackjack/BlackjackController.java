package blackjack;
import javafx.scene.Scene;

public class BlackjackController {

    private final Blackjack blackjackGame;

    public BlackjackController(String username) {
        this.blackjackGame = new Blackjack(username);
    }

    public Scene getMainMenuScene() {
        return blackjackGame.createMainMenuScene();
    }

    public Scene getGameScene() {
        return blackjackGame.createGameScene();
    }

    public Blackjack getBlackjackGame() {
        return blackjackGame;
    }
}
