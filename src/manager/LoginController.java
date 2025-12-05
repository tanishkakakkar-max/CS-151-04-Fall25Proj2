package manager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import storage.FileManager;

/**
 * LoginController handles the login screen UI and logic.
 * Provides user authentication functionality.
 */
public class LoginController {
    
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    /**
     * Constructor creates the login UI.
     */
    public LoginController() {
        createView();
    }

    /**
     * Creates the login screen UI.
     */
    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(50));
        view.setStyle("-fx-background-color: #2C3E50;");

        // Title
        Label titleLabel = new Label("Game Manager");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setStyle("-fx-text-fill: #ECF0F1;");

        // Subtitle
        Label subtitleLabel = new Label("Please login to continue");
        subtitleLabel.setFont(Font.font("Arial", 16));
        subtitleLabel.setStyle("-fx-text-fill: #BDC3C7;");

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: #ECF0F1;");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        usernameField.setStyle("-fx-font-size: 14px;");

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: #ECF0F1;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-font-size: 14px;");

        // Message label for errors/success
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: #E74C3C;");

        // Buttons
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; " +
                           "-fx-font-size: 14px; -fx-padding: 10 30;");
        loginButton.setOnAction(e -> handleLogin());

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " +
                                    "-fx-font-size: 14px; -fx-padding: 10 30;");
        createAccountButton.setOnAction(e -> GameManager.showCreateAccountScreen());

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, createAccountButton);

        // Add all components to view
        view.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            messageLabel,
            buttonBox
        );

        // Enter key triggers login
        passwordField.setOnAction(e -> handleLogin());
    }

    /**
     * Handles the login button action.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validate input
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter a password");
            return;
        }

        // Validate credentials
        FileManager fileManager = GameManager.getFileManager();
        if (fileManager.validateLogin(username, password)) {
            GameManager.setCurrentUser(username);
            GameManager.showMainMenu();
        } else {
            showError("Invalid username or password");
            passwordField.clear();
        }
    }

    /**
     * Displays an error message.
     * @param message the error message
     */
    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #E74C3C;");
    }

    /**
     * Gets the view for this controller.
     * @return the VBox view
     */
    public VBox getView() {
        return view;
    }

    /**
     * Clears all input fields.
     */
    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }

    /**
     * Sets focus to username field.
     */
    public void focusUsername() {
        usernameField.requestFocus();
    }
}

