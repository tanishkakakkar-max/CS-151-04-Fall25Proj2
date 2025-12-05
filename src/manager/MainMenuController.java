package manager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * MainMenuController handles the main menu screen UI and logic.
 * Provides game launching and navigation options.
 */
public class MainMenuController {
    
    private VBox view;

    /**
     * Constructor creates the main menu UI.
     */
    public MainMenuController() {
        createView();
    }

    /**
     * Creates the main menu screen UI.
     */
    private void createView() {
        view = new VBox(25);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(50));
        view.setStyle("-fx-background-color: #34495E;");

        // Welcome message
        String username = GameManager.getCurrentUser();
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setStyle("-fx-text-fill: #ECF0F1;");

        // Title
        Label titleLabel = new Label("Select a Game");
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setStyle("-fx-text-fill: #BDC3C7;");

        // Game buttons container
        HBox gameButtons = new HBox(30);
        gameButtons.setAlignment(Pos.CENTER);

        // Blackjack button
        Button blackjackButton = createGameButton("Blackjack", "#E74C3C");
        blackjackButton.setOnAction(e -> GameManager.launchBlackjack());

        // Snake button
        Button snakeButton = createGameButton("Snake", "#27AE60");
        snakeButton.setOnAction(e -> GameManager.launchSnake());

        gameButtons.getChildren().addAll(blackjackButton, snakeButton);

        // Other options
        Button highScoresButton = new Button("View High Scores");
        highScoresButton.setStyle("-fx-background-color: #F39C12; -fx-text-fill: white; " +
                                 "-fx-font-size: 16px; -fx-padding: 15 40;");
        highScoresButton.setOnAction(e -> GameManager.showHighScores());

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; " +
                             "-fx-font-size: 14px; -fx-padding: 10 30;");
        logoutButton.setOnAction(e -> handleLogout());

        view.getChildren().addAll(
            welcomeLabel,
            titleLabel,
            gameButtons,
            highScoresButton,
            logoutButton
        );
    }

    /**
     * Creates a styled game button.
     * @param gameName the name of the game
     * @param color the background color
     * @return styled button
     */
    private Button createGameButton(String gameName, String color) {
        Button button = new Button(gameName);
        button.setPrefSize(200, 120);
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
     * Refreshes the welcome message with current user.
     */
    public void refreshWelcome() {
        // Recreate view to update username
        createView();
    }
}

