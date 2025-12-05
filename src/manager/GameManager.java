package manager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storage.FileManager;

/**
 * GameManager is the main application class that initializes the game system.
 * Handles scene switching and maintains the current logged-in user.
 */
public class GameManager extends Application {
    
    private static Stage primaryStage;
    private static FileManager fileManager;
    private static String currentUser;
    
    // Scene dimensions
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("CS 151 Game Manager");
        primaryStage.setResizable(false);
        
        // Initialize file manager for data persistence
        fileManager = new FileManager();
        
        // Start with login screen
        showLoginScreen();
        
        primaryStage.show();
    }

    /**
     * Shows the login screen.
     */
    public static void showLoginScreen() {
        currentUser = null;
        LoginController loginController = new LoginController();
        Scene scene = new Scene(loginController.getView(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Manager - Login");
    }

    /**
     * Shows the create account screen.
     */
    public static void showCreateAccountScreen() {
        AccountController accountController = new AccountController();
        Scene scene = new Scene(accountController.getView(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Manager - Create Account");
    }

    /**
     * Shows the main menu screen.
     */
    public static void showMainMenu() {
        MainMenuController mainMenuController = new MainMenuController();
        Scene scene = new Scene(mainMenuController.getView(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Manager - Main Menu");
    }

    /**
     * Shows the high scores screen.
     */
    public static void showHighScores() {
        HighScoreController highScoreController = new HighScoreController();
        Scene scene = new Scene(highScoreController.getView(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Manager - High Scores");
    }

    /**
     * Launches the Blackjack game.
     */
    public static void launchBlackjack() {
        // Will be implemented by Person 2
        System.out.println("Launching Blackjack for user: " + currentUser);
    }

    /**
     * Launches the Snake game.
     */
    public static void launchSnake() {
        // Will be implemented by Person 3
        System.out.println("Launching Snake for user: " + currentUser);
    }

    /**
     * Sets the current logged-in user.
     * @param username the username of logged-in user
     */
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    /**
     * Gets the current logged-in user.
     * @return the username
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets the FileManager instance.
     * @return the file manager
     */
    public static FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Gets the primary stage.
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Checks if a user is currently logged in.
     * @return true if a user is logged in
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null && !currentUser.isEmpty();
    }

    /**
     * Main entry point.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
