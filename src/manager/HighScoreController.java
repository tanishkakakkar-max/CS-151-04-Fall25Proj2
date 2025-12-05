package manager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import storage.FileManager;
import storage.HighScore;
import java.util.List;

/**
 * HighScoreController handles the high scores display screen.
 * Shows top 5 scores for both Blackjack and Snake games.
 */
public class HighScoreController {
    
    private VBox view;
    private VBox blackjackScoresBox;
    private VBox snakeScoresBox;

    /**
     * Constructor creates the high scores UI.
     */
    public HighScoreController() {
        createView();
        loadScores();
    }

    /**
     * Creates the high scores screen UI.
     */
    private void createView() {
        view = new VBox(30);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #34495E;");

        // Title
        Label titleLabel = new Label("High Scores");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setStyle("-fx-text-fill: #ECF0F1;");

        // Container for both score lists
        HBox scoresContainer = new HBox(50);
        scoresContainer.setAlignment(Pos.CENTER);

        // Blackjack scores section
        VBox blackjackSection = createScoreSection("Blackjack");
        blackjackScoresBox = (VBox) blackjackSection.getChildren().get(1);

        // Snake scores section
        VBox snakeSection = createScoreSection("Snake");
        snakeScoresBox = (VBox) snakeSection.getChildren().get(1);

        scoresContainer.getChildren().addAll(blackjackSection, snakeSection);

        // Refresh button
        Button refreshButton = new Button("Refresh Scores");
        refreshButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " +
                              "-fx-font-size: 14px; -fx-padding: 10 20;");
        refreshButton.setOnAction(e -> loadScores());

        // Back button
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; " +
                           "-fx-font-size: 14px; -fx-padding: 10 20;");
        backButton.setOnAction(e -> GameManager.showMainMenu());

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(refreshButton, backButton);

        view.getChildren().addAll(titleLabel, scoresContainer, buttonBox);
    }

    /**
     * Creates a score section for a specific game.
     * @param gameName the name of the game
     * @return VBox containing the score section
     */
    private VBox createScoreSection(String gameName) {
        VBox section = new VBox(10);
        section.setAlignment(Pos.TOP_CENTER);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: #2C3E50; -fx-background-radius: 10;");
        section.setPrefWidth(300);

        // Section title
        Label sectionTitle = new Label(gameName + " Top 5");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        sectionTitle.setStyle("-fx-text-fill: #F39C12;");

        // Scores list
        VBox scoresList = new VBox(8);
        scoresList.setAlignment(Pos.CENTER_LEFT);
        scoresList.setPadding(new Insets(10));

        section.getChildren().addAll(sectionTitle, scoresList);
        return section;
    }

    /**
     * Loads and displays scores from file.
     */
    private void loadScores() {
        FileManager fileManager = GameManager.getFileManager();
        fileManager.loadHighScores(); // Refresh from file

        // Load Blackjack scores
        List<HighScore> blackjackScores = fileManager.getTop5Scores("blackjack");
        displayScores(blackjackScoresBox, blackjackScores);

        // Load Snake scores
        List<HighScore> snakeScores = fileManager.getTop5Scores("snake");
        displayScores(snakeScoresBox, snakeScores);
    }

    /**
     * Displays a list of scores in a VBox.
     * @param container the container to display scores in
     * @param scores the list of scores
     */
    private void displayScores(VBox container, List<HighScore> scores) {
        container.getChildren().clear();

        if (scores.isEmpty()) {
            Label noScores = new Label("No scores yet");
            noScores.setStyle("-fx-text-fill: #BDC3C7; -fx-font-style: italic;");
            container.getChildren().add(noScores);
            return;
        }

        int rank = 1;
        for (HighScore score : scores) {
            Label scoreLabel = new Label(score.toDisplayString(rank));
            scoreLabel.setFont(Font.font("Arial", 16));
            
            // Different color for top 3
            if (rank == 1) {
                scoreLabel.setStyle("-fx-text-fill: #F1C40F;"); // Gold
            } else if (rank == 2) {
                scoreLabel.setStyle("-fx-text-fill: #BDC3C7;"); // Silver
            } else if (rank == 3) {
                scoreLabel.setStyle("-fx-text-fill: #CD6155;"); // Bronze
            } else {
                scoreLabel.setStyle("-fx-text-fill: #ECF0F1;");
            }
            
            container.getChildren().add(scoreLabel);
            rank++;
        }
    }

    /**
     * Updates scores after a game ends (called from game controllers).
     * @param username the player's username
     * @param score the score achieved
     * @param gameType the type of game
     */
    public static void updateScore(String username, int score, String gameType) {
        FileManager fileManager = GameManager.getFileManager();
        fileManager.addHighScore(username, score, gameType);
    }

    /**
     * Gets the view for this controller.
     * @return the VBox view
     */
    public VBox getView() {
        return view;
    }

    /**
     * Gets the top score for a specific game.
     * @param gameType the type of game
     * @return the top score or null if none exists
     */
    public static HighScore getTopScore(String gameType) {
        FileManager fileManager = GameManager.getFileManager();
        List<HighScore> scores = fileManager.getTop5Scores(gameType);
        return scores.isEmpty() ? null : scores.get(0);
    }
}

