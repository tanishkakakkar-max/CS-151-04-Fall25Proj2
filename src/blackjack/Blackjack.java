package blackjack;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.application.Platform;
import manager.HighScoreController;

import java.util.List;

public class Blackjack {

    private static final int STARTING_BALANCE = 1000;
    private static final int MIN_BET = 10;

    private final String username;
    private final Deck deck = new Deck();

    private final Player player;
    private final BotPlayer bot1;
    private final BotPlayer bot2;
    private final Dealer dealer;

    // 0 = human, 1 = AI1, 2 = AI2, 3 = dealer
    private int currentTurnIndex = 0;
    private boolean roundActive = false;

    // UI elements
    private Label statusLabel;
    private Label turnLabel;
    private TextField betField;
    private Button hitButton;
    private Button standButton;
    private Button dealButton;
    private Button saveButton;
    private TextArea saveOutputArea;

    private VBox playerBox;
    private VBox ai1Box;
    private VBox ai2Box;
    private VBox dealerBox;

    public Blackjack(String username) {
        this.username = username;
        player = new HumanPlayer(username, STARTING_BALANCE);
        bot1 = new BotPlayer("AI 1", STARTING_BALANCE, 16);
        bot2 = new BotPlayer("AI 2", STARTING_BALANCE, 18);
        dealer = new Dealer(0); // dealer balance not needed
    }

    // === accessors for SaveGame ===

    public Player getPlayer() {
        return player;
    }

    public BotPlayer getBot1() {
        return bot1;
    }

    public BotPlayer getBot2() {
        return bot2;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }

    public void setCurrentTurnIndex(int index) {
        this.currentTurnIndex = index;
    }

    public void setRoundActive(boolean active) {
        this.roundActive = active;
    }

    // === Scenes ===

    public Scene createMainMenuScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Text title = new Text("Blackjack");
        title.setFont(Font.font(32));

        Label welcome = new Label("Welcome, " + username + "!");
        welcome.setFont(Font.font(18));

        Button startButton = new Button("Start New Game");
        startButton.setOnAction(e -> {
            Scene gameScene = createGameScene();
            startNewRound();
            Stage stage = (Stage) startButton.getScene().getWindow();
            
            // Wrap game scene with toolbar
            BorderPane rootWithToolbar = new BorderPane();
            manager.Toolbar toolbar = new manager.Toolbar(() -> {
                manager.GameManager.showMainMenu();
            });
            rootWithToolbar.setTop(toolbar);
            rootWithToolbar.setCenter(gameScene.getRoot());
            
            Scene sceneWithToolbar = new Scene(rootWithToolbar, gameScene.getWidth(), gameScene.getHeight() + 50);
            stage.setScene(sceneWithToolbar);
        });

        Label loadLabel = new Label("Load game from saveStateString:");
        TextArea loadArea = new TextArea();
        loadArea.setPromptText("Paste saveStateString here...");
        loadArea.setPrefRowCount(3);

        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(e -> {
            String text = loadArea.getText().trim();
            if (text.isEmpty()) {
                showAlert("Error", "Please paste a save state string.");
                return;
            }
            try {
                SaveGame.loadFromSaveState(this, text);
                Scene gameScene = createGameScene();
                Stage stage = (Stage) loadButton.getScene().getWindow();
                
                // Wrap game scene with toolbar
                BorderPane rootWithToolbar = new BorderPane();
                manager.Toolbar toolbar = new manager.Toolbar(() -> {
                    manager.GameManager.showMainMenu();
                });
                rootWithToolbar.setTop(toolbar);
                rootWithToolbar.setCenter(gameScene.getRoot());
                
                Scene sceneWithToolbar = new Scene(rootWithToolbar, gameScene.getWidth(), gameScene.getHeight() + 50);
                stage.setScene(sceneWithToolbar);

                updateAllPlayerViews();
                updateTurnLabel();
                
                // Restore button states based on game state
                if (roundActive) {
                    if (currentTurnIndex == 0) {
                        // Player's turn - enable hit/stand
                        hitButton.setDisable(false);
                        standButton.setDisable(false);
                        dealButton.setDisable(true);
                        statusLabel.setText("Game loaded. Your turn!");
                    } else {
                        // Not player's turn - continue game automatically
                        hitButton.setDisable(true);
                        standButton.setDisable(true);
                        dealButton.setDisable(true);
                        statusLabel.setText("Game loaded. Continuing...");
                        // Continue the game from where it left off
                        // Use Platform.runLater to ensure UI is ready
                        Platform.runLater(() -> continueGameAfterLoad());
                    }
                } else {
                    // Round not active - ready for new round
                    hitButton.setDisable(true);
                    standButton.setDisable(true);
                    dealButton.setDisable(false);
                    statusLabel.setText("Game loaded. Place your bet and press Deal to start.");
                }
            } catch (IllegalArgumentException ex) {
                showAlert("Load Failed", ex.getMessage());
            }
        });

        root.getChildren().addAll(
                title,
                welcome,
                startButton,
                new Separator(),
                loadLabel,
                loadArea,
                loadButton
        );

        return new Scene(root, 900, 600);
    }

    public Scene createGameScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top
        VBox topBox = new VBox(5);
        topBox.setAlignment(Pos.CENTER_LEFT);

        turnLabel = new Label("Turn: ");
        turnLabel.setFont(Font.font(16));

        statusLabel = new Label("Place your bet and press Deal to start.");
        statusLabel.setFont(Font.font(14));
        statusLabel.setTextFill(Color.DARKBLUE);

        topBox.getChildren().addAll(turnLabel, statusLabel);
        root.setTop(topBox);

        // Center: player boxes
        HBox playersRow = new HBox(15);
        playersRow.setAlignment(Pos.CENTER);
        playersRow.setPadding(new Insets(10));

        playerBox = createParticipantBox(player);
        ai1Box = createParticipantBox(bot1);
        ai2Box = createParticipantBox(bot2);
        dealerBox = createParticipantBox(dealer);

        playersRow.getChildren().addAll(playerBox, ai1Box, ai2Box, dealerBox);
        root.setCenter(playersRow);

        // Bottom: controls
        VBox bottomBox = new VBox(8);
        bottomBox.setPadding(new Insets(8));
        bottomBox.setAlignment(Pos.CENTER_LEFT);

        HBox betRow = new HBox(5);
        Label betLabel = new Label("Bet amount ($" + MIN_BET + " min):");
        betField = new TextField(String.valueOf(MIN_BET));
        betField.setPrefWidth(80);
        dealButton = new Button("Deal");
        dealButton.setOnAction(e -> onDeal());

        betRow.getChildren().addAll(betLabel, betField, dealButton);

        HBox actionRow = new HBox(10);
        hitButton = new Button("Hit");
        hitButton.setOnAction(e -> onHit());
        standButton = new Button("Stand");
        standButton.setOnAction(e -> onStand());
        hitButton.setDisable(true);
        standButton.setDisable(true);

        saveButton = new Button("Save Game");
        saveButton.setOnAction(e -> onSave());

        actionRow.getChildren().addAll(hitButton, standButton, saveButton);

        saveOutputArea = new TextArea();
        saveOutputArea.setPromptText("Save state string will appear here when you click 'Save Game'.");
        saveOutputArea.setPrefRowCount(2);

        bottomBox.getChildren().addAll(betRow, actionRow, new Label("Save State:"), saveOutputArea);

        root.setBottom(bottomBox);

        updateAllPlayerViews();
        updateTurnLabel();

        return new Scene(root, 1000, 650);
    }

    // === UI helpers ===

    private VBox createParticipantBox(Player participant) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(8));
        box.setAlignment(Pos.TOP_CENTER);
        box.setPrefWidth(220);
        box.setStyle("-fx-border-color: black; -fx-border-radius: 4; -fx-border-width: 1;");

        Label nameLabel = new Label(participant.getName());
        nameLabel.setFont(Font.font(16));

        Label balanceLabel = new Label("Balance: $" + participant.getBalance());
        Label betLabel = new Label("Bet: $" + participant.getCurrentBet());
        Label valueLabel = new Label("Value: " + participant.getHandValue());

        FlowPane cardsPane = new FlowPane();
        cardsPane.setHgap(5);
        cardsPane.setVgap(5);
        cardsPane.setPrefWrapLength(200);

        box.getChildren().addAll(nameLabel, balanceLabel, betLabel, valueLabel, new Separator(), cardsPane);
        box.setUserData(new ParticipantViewData(balanceLabel, betLabel, valueLabel, cardsPane));
        return box;
    }

    private static class ParticipantViewData {
        Label balanceLabel;
        Label betLabel;
        Label valueLabel;
        FlowPane cardsPane;

        ParticipantViewData(Label balanceLabel, Label betLabel, Label valueLabel, FlowPane cardsPane) {
            this.balanceLabel = balanceLabel;
            this.betLabel = betLabel;
            this.valueLabel = valueLabel;
            this.cardsPane = cardsPane;
        }
    }

    private void updateParticipantBox(VBox box, Player participant, boolean hideDealerSecondCard) {
        ParticipantViewData data = (ParticipantViewData) box.getUserData();
        data.balanceLabel.setText("Balance: $" + participant.getBalance());
        data.betLabel.setText("Bet: $" + participant.getCurrentBet());

        if (participant == dealer && hideDealerSecondCard && participant.getHand().size() > 1) {
            data.valueLabel.setText("Value: ??");
        } else {
            data.valueLabel.setText("Value: " + participant.getHandValue());
        }

        data.cardsPane.getChildren().clear();
        List<Card> hand = participant.getHand();

        for (int i = 0; i < hand.size(); i++) {
            Label cardLabel;
            if (participant == dealer && hideDealerSecondCard && i == 1) {
                cardLabel = createCardLabel("??");
            } else {
                cardLabel = createCardLabel(hand.get(i).toCode());
            }
            data.cardsPane.getChildren().add(cardLabel);
        }
    }

    private Label createCardLabel(String text) {
        Label l = new Label(text);
        l.setPadding(new Insets(5));
        l.setPrefWidth(40);
        l.setAlignment(Pos.CENTER);
        l.setStyle("-fx-border-color: gray; -fx-background-color: white;");
        return l;
    }

    private void updateAllPlayerViews() {
        boolean hideDealerSecond = roundActive && currentTurnIndex != 3;
        updateParticipantBox(playerBox, player, hideDealerSecond);
        updateParticipantBox(ai1Box, bot1, hideDealerSecond);
        updateParticipantBox(ai2Box, bot2, hideDealerSecond);
        updateParticipantBox(dealerBox, dealer, hideDealerSecond);
    }

    private void updateTurnLabel() {
        String who;
        switch (currentTurnIndex) {
            case 0 -> who = player.getName();
            case 1 -> who = bot1.getName();
            case 2 -> who = bot2.getName();
            case 3 -> who = dealer.getName();
            default -> who = "?";
        }
        turnLabel.setText("Turn: " + who);
    }

    // === Game flow ===

    private void onDeal() {
        if (roundActive) {
            statusLabel.setText("Round already in progress.");
            return;
        }

        try {
            int bet = Integer.parseInt(betField.getText().trim());
            if (bet < MIN_BET) {
                showAlert("Invalid Bet", "Bet must be at least $" + MIN_BET);
                return;
            }
            if (bet > player.getBalance()) {
                showAlert("Invalid Bet", "You don't have enough balance.");
                return;
            }

            player.resetForNewRound();
            bot1.resetForNewRound();
            bot2.resetForNewRound();
            dealer.resetForNewRound();

            player.placeBet(bet);
            bot1.placeBet(bet);
            bot2.placeBet(bet);

            // initial deal 2 cards each
            for (int i = 0; i < 2; i++) {
                player.addCard(deck.drawCard());
                bot1.addCard(deck.drawCard());
                bot2.addCard(deck.drawCard());
                dealer.addCard(deck.drawCard());
            }

            roundActive = true;
            currentTurnIndex = 0;
            updateAllPlayerViews();
            updateTurnLabel();

            hitButton.setDisable(false);
            standButton.setDisable(false);
            dealButton.setDisable(true);

            statusLabel.setText("Round started. Your move!");

            checkInitialBlackjack();

        } catch (NumberFormatException e) {
            showAlert("Invalid Bet", "Please enter a numeric bet amount.");
        } catch (IllegalArgumentException e) {
            showAlert("Bet Error", e.getMessage());
        }
    }

    private void checkInitialBlackjack() {
        boolean playerBJ = player.hasBlackjack();
        boolean bot1BJ = bot1.hasBlackjack();
        boolean bot2BJ = bot2.hasBlackjack();
        boolean dealerBJ = dealer.hasBlackjack();

        // If no one has blackjack, continue normal play
        if (!playerBJ && !bot1BJ && !bot2BJ && !dealerBJ) {
            return;
        }

        // Handle dealer blackjack - round ends for everyone
        if (dealerBJ) {
            if (playerBJ) {
                // Both player and dealer have blackjack - push for player
                player.pushBet();
            } else {
                // Only dealer has blackjack - player loses
                player.loseBet();
            }
            
            if (bot1BJ) {
                bot1.pushBet();
            } else {
                bot1.loseBet();
            }
            
            if (bot2BJ) {
                bot2.pushBet();
            } else {
                bot2.loseBet();
            }
            
            statusLabel.setText("Dealer has Blackjack. " + 
                (playerBJ ? "You push." : "Everyone loses."));
            
            // Round ends when dealer has blackjack
            roundActive = false;
            hitButton.setDisable(true);
            standButton.setDisable(true);
            dealButton.setDisable(false);
            updateAllPlayerViews();
            return;
        }
        
        // Dealer doesn't have blackjack - handle player blackjacks
        // Players with blackjack win immediately, but round continues for others
        StringBuilder msg = new StringBuilder();
        boolean allPlayersHaveBJ = playerBJ && bot1BJ && bot2BJ;
        
        if (playerBJ) {
            player.winBet();
            player.stand(); // Player with blackjack is done
            msg.append("You have Blackjack! ");
        }
        if (bot1BJ) {
            bot1.winBet();
            bot1.stand(); // Bot1 with blackjack is done
            msg.append("AI 1 has Blackjack! ");
        }
        if (bot2BJ) {
            bot2.winBet();
            bot2.stand(); // Bot2 with blackjack is done
            msg.append("AI 2 has Blackjack! ");
        }
        
        if (msg.length() > 0) {
            msg.append("Round continues for others.");
            statusLabel.setText(msg.toString());
        }
        
        // If all players have blackjack, round ends
        if (allPlayersHaveBJ) {
            roundActive = false;
            hitButton.setDisable(true);
            standButton.setDisable(true);
            dealButton.setDisable(false);
        } else {
            // Round continues for players without blackjack
            // If player has blackjack, they're done - advance to next turn
            if (playerBJ) {
                // Player is done, advance to bots
                advanceTurn();
            }
            // Otherwise, player can still play (buttons already enabled)
        }
        
        updateAllPlayerViews();
    }

    private void onHit() {
        if (!roundActive || currentTurnIndex != 0) {
            return;
        }
        player.addCard(deck.drawCard());
        updateAllPlayerViews();
        if (player.isBusted()) {
            statusLabel.setText("You busted! Round continues for others.");
            // Disable player buttons since they're done
            hitButton.setDisable(true);
            standButton.setDisable(true);
            // Advance to next turn so bots and dealer can still play
            advanceTurn();
        }
    }

    private void onStand() {
        if (!roundActive || currentTurnIndex != 0) {
            return;
        }
        player.stand();
        advanceTurn();
    }

    private void advanceTurn() {
        currentTurnIndex++;

        while (roundActive && currentTurnIndex <= 3) {
            updateTurnLabel();
            if (currentTurnIndex == 1) {
                autoPlay(bot1);
                currentTurnIndex++; // Move to next turn
            } else if (currentTurnIndex == 2) {
                autoPlay(bot2);
                currentTurnIndex++; // Move to next turn
            } else if (currentTurnIndex == 3) {
                autoPlay(dealer);
                endRound();
                break;
            }
        }
        updateAllPlayerViews();
    }
    
    private void continueGameAfterLoad() {
        // Continue the game from the current turn index
        // This is called after loading a save state when it's not the player's turn
        if (!roundActive) {
            return;
        }
        
        // Set standing flags based on turn index
        // If turnIndex > 0, player has already stood
        if (currentTurnIndex > 0) {
            player.stand();
        }
        // If turnIndex > 1, bot1 has already played (stood or busted)
        if (currentTurnIndex > 1 && !bot1.isBusted()) {
            bot1.stand();
        }
        // If turnIndex > 2, bot2 has already played (stood or busted)
        if (currentTurnIndex > 2 && !bot2.isBusted()) {
            bot2.stand();
        }
        
        // If it's already the dealer's turn, play dealer and end round
        if (currentTurnIndex == 3) {
            updateTurnLabel();
            autoPlay(dealer);
            endRound();
            return;
        }
        
        // Continue from current turn
        while (roundActive && currentTurnIndex <= 3) {
            updateTurnLabel();
            if (currentTurnIndex == 1) {
                autoPlay(bot1);
                currentTurnIndex++; // Move to next turn
            } else if (currentTurnIndex == 2) {
                autoPlay(bot2);
                currentTurnIndex++; // Move to next turn
            } else if (currentTurnIndex == 3) {
                autoPlay(dealer);
                endRound();
                break;
            }
        }
        updateAllPlayerViews();
    }

    private void autoPlay(Player participant) {
        while (participant.shouldHit()) {
            participant.addCard(deck.drawCard());
        }
    }

    private void endRound() {
        roundActive = false;
        hitButton.setDisable(true);
        standButton.setDisable(true);
        dealButton.setDisable(false);

        updateAllPlayerViews();

        int dealerValue = dealer.getHandValue();
        if (dealerValue > 21) {
            statusLabel.setText("Dealer busted! All non-busted players win.");
            settleAgainstDealerBust();
        } else {
            settleAgainstDealer();
        }
        
        // Save high score (use player's balance as score)
        String currentUser = manager.GameManager.getCurrentUser();
        if (currentUser != null && !currentUser.isEmpty()) {
            int playerBalance = player.getBalance();
            HighScoreController.updateScore(currentUser, playerBalance, "blackjack");
        }
        
        // Reshuffle deck after round ends
        deck.reset();
    }

    private void settleAgainstDealerBust() {
        for (Player p : List.of(player, bot1, bot2)) {
            // Skip players who already have blackjack - they've already been paid
            if (p.hasBlackjack()) {
                continue;
            }
            
            if (!p.isBusted()) {
                p.winBet();
            } else {
                p.loseBet();
            }
        }
        updateAllPlayerViews();
    }

    private void settleAgainstDealer() {
        int dealerValue = dealer.getHandValue();
        StringBuilder sb = new StringBuilder();

        for (Player p : List.of(player, bot1, bot2)) {
            // Skip players who already have blackjack - they've already been paid
            if (p.hasBlackjack()) {
                continue;
            }
            
            int value = p.getHandValue();

            if (p.isBusted()) {
                p.loseBet();
                if (p == player) sb.append("You busted. ");
            } else if (value > dealerValue) {
                p.winBet();
                if (p == player) sb.append("You win! ");
            } else if (value < dealerValue) {
                p.loseBet();
                if (p == player) sb.append("You lose. ");
            } else {
                p.pushBet();
                if (p == player) sb.append("Push (tie). ");
            }
        }

        if (sb.length() == 0) {
            sb.append("Round over.");
        }
        statusLabel.setText(sb.toString());
        updateAllPlayerViews();
    }

    private void onSave() {
        String state = SaveGame.createSaveState(this);
        saveOutputArea.setText(state);
        statusLabel.setText("Save State.");
    }

    private void startNewRound() {
        roundActive = false;
        hitButton.setDisable(true);
        standButton.setDisable(true);
        dealButton.setDisable(false);
        statusLabel.setText("Enter bet Deal to start:");
        currentTurnIndex = 0;

        player.resetForNewRound();
        bot1.resetForNewRound();
        bot2.resetForNewRound();
        dealer.resetForNewRound();
        updateAllPlayerViews();
        updateTurnLabel();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}