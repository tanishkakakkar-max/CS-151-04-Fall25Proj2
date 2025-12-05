package snake;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class PauseOverlay extends StackPane {

    public PauseOverlay() {
        setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        Text pausedText = new Text("PAUSED");
        pausedText.setFont(Font.font(40));
        pausedText.setFill(Color.WHITE);
        pausedText.setStyle("-fx-font-weight: bold;");

        setAlignment(Pos.CENTER);
        getChildren().add(pausedText);
    }
}