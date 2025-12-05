package manager;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {

    private Button mainMenuButton;

    public Toolbar(Runnable goToMainMenu) {

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> goToMainMenu.run());

        setSpacing(15);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().add(mainMenuButton);

        setStyle("-fx-background-color: #222; -fx-padding: 8;");
    }
}