package manager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import storage.FileManager;
import storage.HighScore;
import java.util.List;

/**
 * MainMenuController handles the main menu screen UI and logic.
 * Displays high scores on one side and game selection on the other.
 */
public class MainMenuController {
    
    private VBox view;
    private VBox blackjackScoresBox;
    private VBox snakeScoresBox;

    /**
     * Constructor creates the main menu UI.
     */
    public MainMenuController() {
        createView();
        loadScores();
    }

    /**
     * Creates the main menu screen UI with two visually separate areas.
     */
    private void createView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #34495E;");

        // Top: Welcome message and logout button
        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        
        String username = GameManager.getCurrentUser();
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setStyle("-fx-text-fill: #ECF0F1;");
        
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; " +
                             "-fx-font-size: 14px; -fx-padding: 10 30;");
        logoutButton.setOnAction(e -> handleLogout());
        
        topBox.getChildren().addAll(welcomeLabel, logoutButton);
        root.setTop(topBox);

        // Center: Split into two areas
        HBox centerBox = new HBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        // Left side: High Scores
        VBox scoresSection = createScoresSection();
        
        // Right side: Game Selection
        VBox gamesSection = createGamesSection();

        centerBox.getChildren().addAll(scoresSection, gamesSection);
        root.setCenter(centerBox);

        view = new VBox();
        view.getChildren().add(root);
    }

    /**
     * Creates the high scores section.
     */
    private VBox createScoresSection() {
        VBox scoresSection = new VBox(20);
        scoresSection.setAlignment(Pos.TOP_CENTER);
        scoresSection.setPadding(new Insets(20));
        scoresSection.setStyle("-fx-background-color: #2C3E50; -fx-background-radius: 10;");
        scoresSection.setPrefWidth(350);

        Label sectionTitle = new Label("High Scores");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        sectionTitle.setStyle("-fx-text-fill: #ECF0F1;");

        // Blackjack scores
        VBox blackjackSection = createScoreSubSection("Blackjack");
        blackjackScoresBox = (VBox) blackjackSection.getChildren().get(1);

        // Snake scores
        VBox snakeSection = createScoreSubSection("Snake");
        snakeScoresBox = (VBox) snakeSection.getChildren().get(1);

        scoresSection.getChildren().addAll(sectionTitle, blackjackSection, snakeSection);
        return scoresSection;
    }

    /**
     * Creates a score subsection for a specific game.
     */
    private VBox createScoreSubSection(String gameName) {
        VBox section = new VBox(8);
        section.setAlignment(Pos.TOP_CENTER);
        section.setPadding(new Insets(10));

        Label sectionTitle = new Label(gameName + " Top 5");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        sectionTitle.setStyle("-fx-text-fill: #F39C12;");

        VBox scoresList = new VBox(5);
        scoresList.setAlignment(Pos.CENTER_LEFT);
        scoresList.setPadding(new Insets(5));

        section.getChildren().addAll(sectionTitle, scoresList);
        return section;
    }

    /**
     * Creates the game selection section.
     */
    private VBox createGamesSection() {
        VBox gamesSection = new VBox(20);
        gamesSection.setAlignment(Pos.TOP_CENTER);
        gamesSection.setPadding(new Insets(20));
        gamesSection.setStyle("-fx-background-color: #2C3E50; -fx-background-radius: 10;");
        gamesSection.setPrefWidth(400);

        Label sectionTitle = new Label("Select a Game");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        sectionTitle.setStyle("-fx-text-fill: #ECF0F1;");

        VBox gameButtons = new VBox(15);
        gameButtons.setAlignment(Pos.CENTER);
        gameButtons.setPadding(new Insets(10));

        // Blackjack button
        Button blackjackButton = createGameButton("Blackjack", "#E74C3C");
        blackjackButton.setOnAction(e -> GameManager.launchBlackjack());

        // Snake button
        Button snakeButton = createGameButton("Snake", "#27AE60");
        snakeButton.setOnAction(e -> GameManager.launchSnake());

        // Placeholder buttons for future games
        Button placeholder1 = createPlaceholderButton("Game 3");
        Button placeholder2 = createPlaceholderButton("Game 4");

        gameButtons.getChildren().addAll(
            blackjackButton,
            snakeButton,
            placeholder1,
            placeholder2
        );

        gamesSection.getChildren().addAll(sectionTitle, gameButtons);
        return gamesSection;
    }

    /**
     * Loads and displays scores from file.
     */
    private void loadScores() {
        FileManager fileManager = GameManager.getFileManager();
        fileManager.loadHighScores();

        // Load Blackjack scores
        List<HighScore> blackjackScores = fileManager.getTop5Scores("blackjack");
        displayScores(blackjackScoresBox, blackjackScores);

        // Load Snake scores
        List<HighScore> snakeScores = fileManager.getTop5Scores("snake");
        displayScores(snakeScoresBox, snakeScores);
    }

    /**
     * Displays a list of scores in a VBox.
     */
    private void displayScores(VBox container, List<HighScore> scores) {
        if (container == null) return;
        
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
            scoreLabel.setFont(Font.font("Arial", 14));
            
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
     * Creates a styled game button.
     * @param gameName the name of the game
     * @param color the background color
     * @return styled button
     */
    private Button createGameButton(String gameName, String color) {
        Button button = new Button(gameName);
        button.setPrefSize(300, 80);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        button.setStyle("-fx-background-color: " + color + "; " +
                       "-fx-text-fill: white; " +
                       "-fx-background-radius: 10;");
        
        // Hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle("-fx-background-color: derive(" + color + ", 20%); " +
                           "-fx-text-fill: white; " +
                           "-fx-background-radius: 10;"));
        button.setOnMouseExited(e -> 
            button.setStyle("-fx-background-color: " + color + "; " +
                           "-fx-text-fill: white; " +
                           "-fx-background-radius: 10;"));
        
        return button;
    }

    /**
     * Creates a placeholder button for future games.
     * @param label the label text
     * @return styled placeholder button
     */
    private Button createPlaceholderButton(String label) {
        Button button = new Button(label);
        button.setPrefSize(300, 80);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        button.setStyle("-fx-background-color: #7F8C8D; " +
                       "-fx-text-fill: white; " +
                       "-fx-background-radius: 10;");
        button.setDisable(true);
        button.setOpacity(0.6);
        
        return button;
    }

    /**
     * Handles the logout action.
     */
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be returned to the login screen.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            GameManager.setCurrentUser(null);
            GameManager.showLoginScreen();
        }
    }

    /**
     * Gets the view for this controller.
     * @return the VBox view
     */
    public VBox getView() {
        return view;
    }

    /**
     * Refreshes the welcome message with current user and reloads scores.
     */
    public void refreshWelcome() {
        // Recreate view to update username and scores
        createView();
        loadScores();
    }
}

