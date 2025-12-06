package manager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import storage.FileManager;


public class AccountController {
    
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;

  
    public AccountController() {
        createView();
    }

    /**
     * Creates the account creation screen UI.
     */
    private void createView() {
        view = new VBox(15);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(50));
        view.setStyle("-fx-background-color: #2C3E50;");

        // Title
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setStyle("-fx-text-fill: #ECF0F1;");

        // Subtitle
        Label subtitleLabel = new Label("Enter your details to register");
        subtitleLabel.setFont(Font.font("Arial", 16));
        subtitleLabel.setStyle("-fx-text-fill: #BDC3C7;");

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: #ECF0F1;");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username (min 3 characters)");
        usernameField.setMaxWidth(300);
        usernameField.setStyle("-fx-font-size: 14px;");

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: #ECF0F1;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password (min 4 characters)");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-font-size: 14px;");

        // Confirm password field
        Label confirmLabel = new Label("Confirm Password:");
        confirmLabel.setStyle("-fx-text-fill: #ECF0F1;");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter password");
        confirmPasswordField.setMaxWidth(300);
        confirmPasswordField.setStyle("-fx-font-size: 14px;");

        // Message label for errors/success
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: #E74C3C;");

        // Buttons
        Button createButton = new Button("Create Account");
        createButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; " +
                             "-fx-font-size: 14px; -fx-padding: 10 30;");
        createButton.setOnAction(e -> handleCreateAccount());

        Button backButton = new Button("Back to Login");
        backButton.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; " +
                           "-fx-font-size: 14px; -fx-padding: 10 30;");
        backButton.setOnAction(e -> GameManager.showLoginScreen());

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(createButton, backButton);

        // Add all components to view
        view.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            confirmLabel,
            confirmPasswordField,
            messageLabel,
            buttonBox
        );

        // Enter key triggers create account
        confirmPasswordField.setOnAction(e -> handleCreateAccount());
    }

   
    private void handleCreateAccount() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate username
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        if (username.length() < 3) {
            showError("Username must be at least 3 characters");
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            showError("Please enter a password");
            return;
        }

        if (password.length() < 4) {
            showError("Password must be at least 4 characters");
            return;
        }

        // Check password confirmation
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            confirmPasswordField.clear();
            return;
        }

        // Check for duplicate username
        FileManager fileManager = GameManager.getFileManager();
        if (fileManager.userExists(username)) {
            showError("Username already exists");
            return;
        }

        // Create the account
        if (fileManager.addUser(username, password)) {
            showSuccess("Account created successfully!");
            // Clear fields
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            // Go back to login after short delay
            GameManager.showLoginScreen();
        } else {
            showError("Failed to create account");
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
     * Displays a success message.
     * @param message the success message
     */
    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #27AE60;");
    }

    /**
     * Gets the view for this controller.
     * @return the VBox view
     */
    public VBox getView() {
        return view;
    }

    
    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        messageLabel.setText("");
    }

    /**
     * Sets focus to username field.
     */
    public void focusUsername() {
        usernameField.requestFocus();
    }
}

