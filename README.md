# CS 151 Game Manager

A JavaFX-based game management application featuring two classic games: **Blackjack** and **Snake**. The application includes user authentication, high score tracking, encrypted save states, and a modern, intuitive user interface.

## Features

### Games

#### Blackjack
- Full-featured Blackjack game with standard rules
- Play against 2 AI bots and a dealer
- Starting balance: $1000
- Minimum bet: $10
- Save and load game states (encrypted)
- Automatic high score tracking (uses balance as score)
- Individual blackjack handling - game continues for other players
- Proper betting mechanics (win 2x, lose bet, push returns bet)

#### Snake
- Classic Snake game with smooth controls
- Starting score: 1000 points
- Pause functionality (ESC key)
- Game over screen with final score and restart option
- Automatic high score tracking
- Collision detection (walls and self-collision)
- Food respawns avoiding snake body

### Security

- **Encrypted Passwords**: All user passwords are encrypted using XOR cipher with Base64 encoding
- **Encrypted Save States**: Blackjack save states are fully encrypted
- **Backward Compatibility**: Automatically migrates old plain-text passwords to encrypted format

### High Scores

- Top 5 scores displayed for each game
- Color-coded rankings (Gold/Silver/Bronze for top 3)
- Scores automatically saved after each game
- Refresh functionality to reload scores from file
- Scores displayed directly on main menu

### Data Persistence

- User accounts stored in `user_accounts.txt`
- High scores stored in `high_scores.txt`
- Blackjack save states can be copied and pasted
- All data files are automatically created if they don't exist

## Project Structure

```
CS151proj/
├── src/
│   ├── main/
│   │   └── Main.java                 # Application entry point
│   ├── manager/
│   │   ├── GameManager.java          # Main application controller
│   │   ├── LoginController.java      # Login screen
│   │   ├── AccountController.java    # Account creation
│   │   ├── MainMenuController.java   # Main menu with high scores
│   │   ├── HighScoreController.java  # High scores screen
│   │   └── Toolbar.java              # Reusable toolbar component
│   ├── blackjack/
│   │   ├── Blackjack.java            # Main game logic
│   │   ├── BlackjackController.java  # Game controller
│   │   ├── Player.java               # Abstract player class
│   │   ├── HumanPlayer.java          # Human player implementation
│   │   ├── BotPlayer.java            # AI bot player
│   │   ├── Dealer.java               # Dealer AI
│   │   ├── Card.java                 # Card representation
│   │   ├── Deck.java                 # Deck management
│   │   └── SaveGame.java             # Save/load functionality
│   ├── snake/
│   │   ├── SnakeGame.java            # Main game logic
│   │   ├── SnakeController.java     # Game controller
│   │   ├── Snake.java                # Snake entity
│   │   ├── SnakeSegment.java        # Snake segment
│   │   ├── Food.java                 # Food entity
│   │   ├── GameBoard.java            # Rendering and collision
│   │   ├── SnakeConstants.java       # Game constants
│   │   └── PauseOverlay.java        # Pause screen
│   ├── storage/
│   │   ├── FileManager.java          # File I/O operations
│   │   ├── User.java                 # User account model
│   │   └── HighScore.java            # High score model
│   └── utils/
│       └── EncryptionUtils.java      # Encryption utilities
├── junit tests/
│   ├── CardTest.java                 # Card class tests
│   ├── EncryptionUtilsTest.java      # Encryption tests
│   └── BotPlayerTest.java            # Bot player tests
├── user_accounts.txt                 # User data (encrypted passwords)
├── high_scores.txt                   # High score records
└── README.md                         # This file
```

## Requirements

- **Java**: JDK 11 or higher
- **JavaFX**: JavaFX SDK 21.0.1 or compatible version
- **JUnit**: JUnit 5 (for running tests)

## How to Run

### Compilation

```bash
# Compile all source files
javac --module-path /path/to/javafx-sdk-21.0.1/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d out $(find src -name "*.java")
```

### Execution

```bash
# Run the application
java --module-path /path/to/javafx-sdk-21.0.1/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp out main.Main
```

### Running Tests

```bash
# Compile test files
javac -d out -cp "out:junit-platform-console-standalone-1.10.0.jar" \
      "junit tests/*.java"

# Run tests
java -cp "out:junit-platform-console-standalone-1.10.0.jar" \
     org.junit.platform.console.ConsoleLauncher \
     --class-path out \
     --select-class CardTest \
     --select-class EncryptionUtilsTest \
     --select-class BotPlayerTest
```

## Usage Guide

### Getting Started

1. **Launch the application** - Run `Main.java` or use the compiled class
2. **Create an account** - Click "Create Account" and enter:
   - Username (minimum 3 characters)
   - Password (minimum 4 characters)
   - Confirm password
3. **Login** - Enter your credentials to access the main menu

### Main Menu

The main menu displays:
- **Left Side**: Top 5 high scores for both Blackjack and Snake games
- **Right Side**: Game selection buttons (Blackjack, Snake, and placeholder buttons for future games)
- **Top**: Welcome message and logout button

### Playing Blackjack

1. Click "Blackjack" from the main menu
2. Enter your bet amount (minimum $10)
3. Click "Deal" to start the round
4. Use "Hit" to take another card or "Stand" to end your turn
5. Watch as AI bots and dealer play automatically
6. Round ends automatically - winnings/losses are calculated
7. Use "Save Game" to copy your encrypted save state
8. Use "Load Game" to paste a save state and continue

**Blackjack Rules:**
- Dealer hits on soft 17 or below
- Blackjack pays 2:1 (same as regular win)
- Push (tie) returns your bet
- Win pays 2x your bet
- Deck reshuffles after each round

### Playing Snake

1. Click "Snake" from the main menu
2. Use arrow keys to control the snake
3. Eat food to grow and increase score
4. Avoid walls and your own body
5. Press ESC to pause/unpause
6. Game over screen shows final score and restart option

**Snake Controls:**
- ↑ ↓ ← → Arrow keys: Change direction
- ESC: Pause/Unpause game
- Restart button: Start a new game after game over

### High Scores

- High scores are automatically saved after each game
- View top 5 scores for each game on the main menu
- Click "View High Scores" for a detailed view with refresh option
- Scores are sorted from highest to lowest

## Technical Details

### Encryption

The application uses a symmetric cipher (XOR + Base64) for:
- **Passwords**: Encrypted before storage in `user_accounts.txt`
- **Save States**: Entire Blackjack game state is encrypted
- **Backward Compatibility**: Old plain-text passwords are automatically encrypted on load

### Save States

Blackjack save states include:
- Current turn index
- All player balances
- Current bets
- All hands (player, bots, dealer)
- Remaining deck state

Save states are encrypted and can be copied/pasted to continue games later.

### AI Behavior

**Bot Players:**
- Bot 1: Hits on 15 or below (hit threshold: 16)
- Bot 2: Hits on 17 or below (hit threshold: 18)
- Both follow standard blackjack strategy

**Dealer:**
- Hits on soft 17 or below
- Stands on hard 17 or higher
- Follows standard casino rules

## File Formats

### user_accounts.txt
```
username,encryptedPassword
```

### high_scores.txt
```
username,score,gameType,timestamp
```

## Testing

The project includes JUnit tests for:
- `Card` class functionality
- `EncryptionUtils` encryption/decryption
- `BotPlayer` decision-making logic

Run tests using the JUnit platform console launcher.

## Known Features

-  User authentication with encrypted passwords
-  Two fully functional games (Blackjack and Snake)
-  High score tracking and display
-  Encrypted save/load for Blackjack
-  AI players with configurable strategies
-  Modern JavaFX UI with responsive design
-  Game state persistence
-  Toolbar navigation from all screens
-  Pause functionality in Snake
-  Restart functionality in Snake
-  Main menu with integrated high scores

## Future Enhancements

- Placeholder buttons on main menu for additional games
- Sound effects (placeholders already in code)
- Enhanced graphics and animations
- Multiplayer support
- Tournament mode
- Statistics tracking

## Credits

Developed for CS 151 - Object-Oriented Design and Programming

## License

This project is developed for educational purposes.

