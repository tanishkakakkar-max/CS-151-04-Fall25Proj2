# CS151 Project 2 — Game Manager + Blackjack + Snake (Java & JavaFX)

A comprehensive JavaFX-based game management platform featuring user authentication, high score tracking, and two fully functional games: Blackjack and Snake. The application implements a modular MVC-style architecture with encrypted data persistence and a modern, intuitive user interface.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Team Contributions](#team-contributions)
3. [Features](#features)
4. [Architecture / Directory Structure](#architecture--directory-structure)
5. [System Design](#system-design)
6. [How to Build and Run the Project](#how-to-build-and-run-the-project)
7. [Usage Guide](#usage-guide)
8. [Demo Video](#demo-video)
9. [Contribution Breakdown](#contribution-breakdown)
10. [Future Improvements](#future-improvements)
11. [License](#license)

---

## Project Overview

This project is a multi-game platform built with JavaFX that provides a complete gaming experience with user management, persistent data storage, and two distinct games. The system is designed with a clean separation of concerns, following MVC (Model-View-Controller) principles.

### Core System Components

**JavaFX Multi-Game Platform**
- Centralized game management through `GameManager` class
- Scene-based navigation system
- Universal toolbar component for consistent UI across all screens
- Modular game integration architecture

**Login + Create Account System**
- Secure user authentication with encrypted password storage
- Account creation with validation (minimum 3 characters for username, 4 for password)
- Session management with current user tracking
- Automatic migration of legacy plain-text passwords to encrypted format

**Universal Toolbar**
- Reusable `Toolbar` component that appears on all game screens
- "Main Menu" button for quick navigation
- Consistent styling and positioning across scenes
- Integrated seamlessly with both Blackjack and Snake games

**High Score Manager**
- Shared, file-based high score tracking system
- Separate leaderboards for Blackjack and Snake games
- Top 5 scores displayed on main menu
- Automatic score saving after each game
- Refresh functionality to reload scores from file
- Color-coded rankings (Gold/Silver/Bronze for top 3)

**Blackjack Game**
- Mouse-only input (buttons for Hit, Stand, Deal, Save, Load)
- Full-featured Blackjack with standard casino rules
- Human player + 2 AI bots + Dealer
- Complete betting system with win/loss/push mechanics
- Encrypted save/load functionality for game states

**Snake Game**
- Keyboard-only input (arrow keys for movement, ESC for pause)
- Classic Snake gameplay with collision detection
- Pause overlay and game over screen
- Restart functionality
- Score tracking integrated with high score system

**Persistent Save/Load for Blackjack**
- Full game state serialization (encrypted)
- Saves: turn index, balances, bets, hands, deck state
- Loads and restores complete game state
- Auto-continue functionality after loading

**Fully Modular MVC-Style Structure**
- Clear package separation: `manager`, `blackjack`, `snake`, `storage`, `utils`
- Controller classes for UI management
- Model classes for game logic and data
- Utility classes for shared functionality

---

## Team Contributions

This project was developed collaboratively by a team of three developers, each responsible for distinct components of the system. The following sections detail the specific contributions of each team member.

### 🧑‍💻 Person 1 — Tanishka (Game Manager + Storage Systems)

**Game Manager Architecture**

Tanishka architected and implemented the entire application flow from user authentication through game selection. She built the complete login → account creation → main menu navigation system using JavaFX's scene-based architecture. This included developing five core controller classes: `LoginController` for user authentication with real-time validation, `AccountController` for new user registration with duplicate checking and password confirmation, `MainMenuController` for the central hub displaying high scores and game selection, `GameManager` as the central orchestrator managing scene transitions and application lifecycle, and `HighScoreController` for managing score persistence and retrieval.

The main menu implementation features a sophisticated split-panel design with the left side displaying the top 5 high scores for both Blackjack and Snake games, automatically sorted from highest to lowest. The right side provides game selection buttons with placeholder slots for future game additions. Tanishka ensured seamless scene switching between all screens, maintaining proper state management and preventing memory leaks through careful JavaFX lifecycle handling.

**Data Storage & Persistence**

Tanishka designed and implemented the secure data persistence layer using `FileManager` and the model classes `User` and `HighScore`. The `FileManager` class handles all file I/O operations for user accounts and high scores, implementing robust error handling and automatic file creation on first launch. She designed a migration system that automatically detects legacy plain-text passwords in existing user accounts and transparently encrypts them during the load process, ensuring backward compatibility while maintaining security.

The high score system implements efficient sorting algorithms to maintain top 5 lists for each game type, with automatic filtering and refresh functionality. The `HighScore` model class stores username, score, game type, and timestamp, while `FileManager` provides methods for adding, retrieving, and sorting scores. Tanishka ensured that all data files (`user_accounts.txt` and `high_scores.txt`) are automatically created if they don't exist, preventing runtime errors on first launch.

**Toolbar Integration**

Tanishka developed the reusable `Toolbar` component (`Toolbar.java`) that provides consistent navigation across all game screens. The toolbar features a "Return to Main Menu" button with clean styling and proper event handling. She ensured the toolbar integrates seamlessly with both Blackjack and Snake games by wrapping game scenes in `BorderPane` containers with the toolbar positioned at the top. This design allows the toolbar to appear consistently across all game screens while maintaining proper focus management for keyboard input in the Snake game.

### 🎮 Person 2 — Piero (Blackjack Game Developer)

**Blackjack Core Logic**

Piero implemented the complete Blackjack game engine in `Blackjack.java`, creating a fully functional casino-style game with proper rule enforcement. He designed the class hierarchy with an abstract `Player` base class and three concrete implementations: `HumanPlayer` for user-controlled gameplay, `BotPlayer` for AI opponents with configurable hit thresholds, and `Dealer` with strict rule adherence (hits on soft 17, stands on hard 17 or higher).

The game loop manages turn-based gameplay with four participants: the human player, two AI bots, and the dealer. Piero implemented sophisticated betting mechanics where players place bets before each round, with money deducted upon betting and returned with winnings or pushes. The win/loss calculation handles all standard Blackjack scenarios: blackjack (3:2 payout), regular wins (1:1), losses, pushes (tie), and bust conditions. He added special logic to handle initial blackjack detection, ensuring players with natural blackjack receive proper payouts even if the dealer also has blackjack.

The AI bot implementation uses threshold-based decision making, where each bot has a configurable `hitThreshold` (typically 16 or 17) that determines when they will request additional cards. The dealer follows strict casino rules, including the soft 17 requirement. Piero implemented proper card dealing logic with the `Deck` class, ensuring cards are shuffled and dealt correctly, with automatic reshuffling after each round.

**Save/Load System**

Piero developed the complete encrypted save/load system using `SaveGame.java`. He designed a comprehensive save string format that serializes the entire game state, including: current turn index, all player balances, active bets for each participant, complete hands for all players and dealer, and the full deck state (both used and remaining cards). The save format uses a compact string representation with pipe delimiters for efficient storage.

The load functionality (`loadFromSaveState`) parses the encrypted save string, validates the data integrity, and reconstructs the complete game state including recreating all card objects, restoring player hands, resetting balances and bets, and restoring the deck to its exact previous state. Piero implemented auto-continue functionality that automatically advances the game after loading if it's not the player's turn, ensuring seamless gameplay resumption. The entire save/load system integrates with `EncryptionUtils` to encrypt save states, preventing users from reading or modifying game data.

**UI Layer**

Piero developed `BlackjackController.java` to bridge the game logic with the JavaFX user interface. He implemented comprehensive button handlers for all game actions: `onHit()` for requesting cards, `onStand()` for ending the player's turn, `onDeal()` for starting new rounds, `onSave()` for persisting game state, and `onLoad()` for restoring saved games. The UI dynamically updates to show all participants' hands, balances, bets, and game status.

He integrated card rendering using JavaFX `Canvas` and `GraphicsContext`, displaying card suits and ranks with proper visual representation. The UI includes real-time updates for turn indicators, status messages, and participant information boxes. Piero ensured proper state management, disabling buttons when appropriate (e.g., disabling Hit/Stand during dealer's turn) and providing clear visual feedback for all game events.

### 🐍 Person 3 — Shresth (Snake Game + Toolbar + Full Integration)

**Snake Game Implementation**

Shresth built the complete Snake game from the ground up, creating a modular architecture with six core classes. `SnakeGame.java` serves as the main game controller, managing the game loop using JavaFX's `AnimationTimer` for smooth 60 FPS gameplay. `SnakeController.java` handles scene creation and initialization, while `Snake.java` manages the snake's body segments and movement logic. `SnakeSegment.java` represents individual body parts with x/y coordinates, `Food.java` handles food spawning and collision detection, and `GameBoard.java` manages the canvas rendering and boundary checking.

The movement system implements a tick-based loop with configurable frame delay (`FRAME_DELAY`) to control game speed. Shresth implemented a sophisticated directional state machine that prevents instant direction reversal, using the `isOpposite()` method to validate direction changes. The keyboard input system captures arrow key presses and updates the snake's direction, with proper focus management to ensure input works even when the game is wrapped in a `BorderPane` with a toolbar.

Collision detection handles two scenarios: wall collisions (checked against board boundaries) and self-collision (head intersecting any body segment). Shresth implemented a special case in the collision logic to prevent false positives when the snake grows after eating food, since the new segment is added at the tail position which the head just vacated. The scoring system starts at 1000 points and increments with each food consumed, automatically saving high scores when the game ends.

**Pause & Game Over Systems**

Shresth implemented `PauseOverlay.java`, a reusable overlay component that displays when the game is paused. The pause functionality is mapped to the ESC key and provides a clean visual overlay with a "Resume" button. The pause system properly suspends the game loop while maintaining all game state.

The game over screen displays the final score prominently and includes a "Restart Game" button. Shresth implemented robust restart functionality that properly resets all game state: creating a new `Snake` instance, resetting the score to 1000, respawning food in a valid location, and most critically, recreating the `AnimationTimer` and key event handlers to ensure the new game instance works correctly. The restart logic removes old event handlers before attaching new ones to prevent conflicts, and uses `Platform.runLater()` to properly restore focus to the game area.

**Integration & UI Polish**

Shresth ensured both games integrate seamlessly into the application's menu system and scene switching architecture. He refactored shared components and constants into appropriate utility classes, ensuring code reusability. He fixed numerous rendering bugs including boundary calculation errors, focus management issues, and event handler conflicts.

The integration work included ensuring the Snake game works correctly when wrapped with the `Toolbar` component, maintaining proper keyboard input focus even when the game scene is embedded in a `BorderPane`. Shresth implemented responsive layout improvements, ensuring the game scales properly and maintains correct aspect ratios. He also fixed the restart functionality through multiple iterations, addressing focus issues, event handler conflicts, and ensuring the key handlers reference the current `snake` instance rather than capturing stale references.

---

## Features

### Game Manager

#### Login System (`LoginController`)
- Username and password authentication
- Real-time validation feedback
- Error message display for invalid credentials
- Navigation to account creation screen
- Enter key support for quick login

#### Account Creation (`AccountController`)
- Username validation (minimum 3 characters, no duplicates)
- Password validation (minimum 4 characters)
- Password confirmation matching
- Automatic redirect to login after successful creation
- Input field validation with clear error messages

#### Main Menu (`MainMenuController`)
- **Left Panel**: Top 5 high scores for both games
  - Blackjack Top 5 (ordered highest to lowest)
  - Snake Top 5 (ordered highest to lowest)
  - Color-coded rankings
  - "No scores yet" message for empty lists
- **Right Panel**: Game selection
  - Blackjack button (functional)
  - Snake button (functional)
  - Game 3 placeholder button (disabled)
  - Game 4 placeholder button (disabled)
- Welcome message with current username
- Logout button with confirmation dialog
- Automatic score refresh on menu load

#### High Score Display (`HighScoreController`)
- Dedicated high scores screen
- Top 5 scores per game displayed side-by-side
- Refresh button to reload from file
- Back to menu navigation
- Timestamp tracking for each score entry

#### File-Based Storage (`FileManager`)
- Automatic file creation (`user_accounts.txt`, `high_scores.txt`)
- User account management (add, validate, load)
- High score management (add, sort, retrieve top N)
- Encrypted password storage
- Backward compatibility for unencrypted passwords
- Thread-safe file operations

#### Scene Switching (`GameManager`)
- Centralized scene management
- Static methods for navigation: `showLoginScreen()`, `showMainMenu()`, `showHighScores()`
- Game launching: `launchBlackjack()`, `launchSnake()`
- Toolbar integration for all game scenes
- Window dimension management (800x600 for menus, custom for games)

### Blackjack Game

#### Human Player Logic (`HumanPlayer`)
- Extends abstract `Player` class
- Manual decision-making via UI buttons
- `shouldHit()` returns false (decisions made through UI)

#### Two Bot Players with Hit-Threshold Strategies (`BotPlayer`)
- **AI 1**: Hit threshold of 16 (hits on 15 or below)
- **AI 2**: Hit threshold of 18 (hits on 17 or below)
- Configurable `hitThreshold` parameter
- Automatic play during their turns
- `shouldHit()` implementation based on hand value vs threshold

#### Dealer Rules (`Dealer`)
- Standard casino dealer behavior
- Hits on soft 17 or below
- Stands on hard 17 or higher
- `shouldHit()` checks for soft 17 (ace counted as 11)

#### Deck + Card Rendering (`Deck`, `Card`)
- Standard 52-card deck (4 suits × 13 ranks)
- Shuffle functionality using `Collections.shuffle()`
- Automatic reset when deck is empty
- Card representation: rank (2-9, T, J, Q, K, A) + suit (H, D, C, S)
- Card display in UI with code format (e.g., "AH", "7C")
- `Card.toCode()` and `Card.fromCode()` for serialization

#### Betting System (`Player`)
- Starting balance: $1000 for all players
- Minimum bet: $10
- `placeBet()`: Deducts bet from balance when placed
- `winBet()`: Adds 2× bet (return bet + winnings)
- `loseBet()`: Bet already deducted, no additional action
- `pushBet()`: Returns bet (tie = no win, no loss)
- Balance displayed in real-time

#### Turn Sequencing (`Blackjack`)
- Turn order: Human (0) → AI 1 (1) → AI 2 (2) → Dealer (3)
- `currentTurnIndex` tracks current player
- `advanceTurn()` automatically progresses through turns
- Human player uses buttons; bots and dealer play automatically
- Turn label updates to show current player

#### Hit/Stand Mechanics
- **Hit Button**: Adds card to hand, checks for bust
- **Stand Button**: Ends player turn, advances to next player
- Bust detection: Hand value > 21
- Button states: Disabled when not player's turn or round inactive
- Status messages for game events

#### Full-Game Save and Load System (`SaveGame`)
- **Save Format** (encrypted):
  ```
  turnIndex|playerBal,bot1Bal,bot2Bal,dealerBal|playerBet,bot1Bet,bot2Bet|playerHand;bot1Hand;bot2Hand;dealerHand|deckCards
  ```
- Example save string structure:
  ```
  2|1000,1000,1000,0|200,200,200|2H,QC,7C;6D,7D,TD;3H,9C,4D,4C;TH,5H,AS,5C|AD,8S,9D,KH,QS,2C,...
  ```
- **Encryption**: Entire save state encrypted using `EncryptionUtils`
- **Load Functionality**:
  - Decrypts save state (with backward compatibility for unencrypted)
  - Restores turn index
  - Restores all balances
  - Restores all bets
  - Restores all hands (player, bots, dealer)
  - Restores deck state
  - Recomputes busted flags
  - Sets round active flag
  - Auto-continues game if not player's turn

#### Restoring State
- Hand values recalculated from restored cards
- Balances and bets fully restored
- Turn index restored (game continues from correct turn)
- Deck state preserved (same card order)
- UI elements updated to reflect restored state
- Button states restored based on turn index

### Snake Game

#### Directions (`Snake.Direction`)
- Enum: `UP`, `DOWN`, `LEFT`, `RIGHT`
- Direction change prevention (cannot reverse into opposite direction)
- Immediate collision check prevents turning into neck segment

#### Movement Loop (`AnimationTimer`)
- Frame-based movement using `AnimationTimer`
- Frame delay: 150,000,000 nanoseconds (~150ms per move)
- Snake moves one cell per frame
- Smooth, consistent movement speed

#### Collision Detection
- **Wall Collision**: Checks if head is outside board boundaries (25×25 grid)
- **Self-Collision**: Checks if head overlaps any body segment
- **Food Collision**: Checks if head position matches food position
- Special handling: Skips self-collision check immediately after eating food (new segment at tail is safe)

#### Game Over Overlay
- Displays "GAME OVER" in red text
- Shows final score
- Provides "Restart Game" button
- Automatically saves high score
- Centered overlay on game board

#### Pause Overlay (`PauseOverlay`)
- Toggle with ESC key
- Overlay displayed when paused
- Game loop continues but movement paused
- Removed when unpaused

#### Food Spawning (`Food`)
- Random spawn within board boundaries
- Ensures food never spawns on snake body
- Respawns immediately after being eaten
- Uses `Random.nextInt()` for position generation

#### Score Tracking
- Starting score: 1000 points
- Increments by 1 for each food eaten
- Displayed in real-time at top of screen
- Saved to high score file on game over
- Reset to 1000 on restart

#### High Score Integration
- Automatic save on game over via `HighScoreController.updateScore()`
- Game type: "snake"
- Username from current session
- Score value from game state

#### Restart Logic (`restartGame()`)
- Removes game over and pause overlays
- Stops old `AnimationTimer`
- Resets snake to center position
- Resets score to 1000
- Respawns food (ensuring not on snake)
- Recreates key event handler (references new snake instance)
- Removes old key handlers to avoid conflicts
- Attaches new handlers to scene, scene root, and game root
- Creates and starts new `AnimationTimer`
- Restores focus to game area using `Platform.runLater()`

---

## Architecture / Directory Structure

```
CS151proj/
├── src/
│   ├── main/
│   │   └── Main.java                    # Application entry point, launches GameManager
│   │
│   ├── manager/                         # Game Manager Package
│   │   ├── GameManager.java             # Main application controller, scene switching, game launching
│   │   ├── LoginController.java        # Login screen UI and authentication logic
│   │   ├── AccountController.java       # Account creation UI and validation
│   │   ├── MainMenuController.java      # Main menu with high scores and game selection
│   │   ├── HighScoreController.java     # High scores display screen
│   │   └── Toolbar.java                 # Reusable toolbar component with Main Menu button
│   │
│   ├── blackjack/                       # Blackjack Game Package
│   │   ├── Blackjack.java               # Main game logic, UI, turn management, round flow
│   │   ├── BlackjackController.java     # Controller wrapper for GameManager integration
│   │   ├── Player.java                  # Abstract base class for all players
│   │   ├── HumanPlayer.java             # Human player implementation
│   │   ├── BotPlayer.java               # AI bot player with configurable hit threshold
│   │   ├── Dealer.java                  # Dealer AI with soft 17 logic
│   │   ├── Card.java                    # Card representation and utilities
│   │   ├── Deck.java                    # Deck management, shuffle, reset
│   │   └── SaveGame.java                # Save/load game state (encrypted)
│   │
│   ├── snake/                           # Snake Game Package
│   │   ├── SnakeGame.java               # Main game logic, game loop, collision detection
│   │   ├── SnakeController.java         # Controller wrapper for GameManager integration
│   │   ├── Snake.java                   # Snake entity with direction and movement
│   │   ├── SnakeSegment.java            # Individual snake segment coordinates
│   │   ├── Food.java                    # Food entity with spawn logic
│   │   ├── GameBoard.java               # Canvas rendering and collision detection
│   │   ├── SnakeConstants.java          # Game configuration constants
│   │   └── PauseOverlay.java            # Pause screen UI component
│   │
│   ├── storage/                         # Data Storage Package
│   │   ├── FileManager.java             # File I/O for users and high scores
│   │   ├── User.java                    # User account model with encrypted password
│   │   ├── HighScore.java               # High score model with timestamp
│   │   └── EncryptionUtils.java         # [DEPRECATED - moved to utils]
│   │
│   └── utils/                           # Utilities Package
│       └── EncryptionUtils.java         # Encryption/decryption utilities (XOR + Base64)
│
├── junit tests/                         # JUnit Test Suite
│   ├── CardTest.java                    # Unit tests for Card class
│   ├── EncryptionUtilsTest.java         # Unit tests for encryption utilities
│   └── BotPlayerTest.java               # Unit tests for BotPlayer logic
│
├── user_accounts.txt                    # User account data (encrypted passwords)
├── high_scores.txt                      # High score records
├── junit-platform-console-standalone-1.10.0.jar  # JUnit test runner
└── README.md                            # This file
```

### Package Responsibilities

**`main`**: Application entry point that delegates to `GameManager`.

**`manager`**: Core application management including scene switching, user session management, and game launching. Contains all UI controllers for the game manager interface.

**`blackjack`**: Complete Blackjack game implementation with player management, betting system, turn sequencing, and save/load functionality.

**`snake`**: Complete Snake game implementation with game loop, collision detection, pause/restart functionality, and score tracking.

**`storage`**: Data persistence layer handling file I/O operations for user accounts and high scores. Includes models for `User` and `HighScore`.

**`utils`**: Shared utility classes including encryption/decryption functionality used across the application.

---

## System Design

### Game Manager Design

#### Scene Switching Architecture
The `GameManager` class uses a centralized scene management system:

```java
// Scene switching methods
showLoginScreen()      → LoginController
showCreateAccountScreen() → AccountController
showMainMenu()         → MainMenuController
showHighScores()       → HighScoreController
launchBlackjack()      → BlackjackController (wrapped with Toolbar)
launchSnake()          → SnakeController (wrapped with Toolbar)
```

**Scene Flow:**
1. Application starts → Login Screen
2. Login successful → Main Menu
3. Create Account → Account Controller → Login Screen
4. Main Menu → Game Selection → Game Screen (with Toolbar)
5. Game Screen → Toolbar "Main Menu" → Main Menu
6. Main Menu → High Scores → High Scores Screen
7. High Scores → Back to Menu → Main Menu

#### UI Flow
- **Login Screen**: Username/password fields, Login button, Create Account button
- **Account Creation**: Username, password, confirm password fields with validation
- **Main Menu**: Split view with high scores (left) and game selection (right)
- **High Scores Screen**: Top 5 for each game, Refresh button, Back button
- **Game Screens**: Wrapped with `Toolbar` at top, game content below

#### File Storage Format

**`user_accounts.txt`**:
```
username,encryptedPassword
```
- Passwords are encrypted using `EncryptionUtils.encrypt()`
- Format: Base64-encoded XOR cipher
- Example: `admin,AbCdEf123456==`

**`high_scores.txt`**:
```
username,score,gameType,timestamp
```
- Format: CSV with timestamp in `yyyy-MM-dd HH:mm:ss`
- Example: `bhuvan,2742,blackjack,2025-12-05 19:27:09`
- Sorted by score (descending) when loaded

#### Toolbar Behavior
- **Fixed Component**: Appears on all game screens (Blackjack and Snake)
- **Reusable**: Single `Toolbar` class used across games
- **Positioning**: Always at top of scene using `BorderPane.setTop()`
- **Functionality**: "Main Menu" button that calls `GameManager.showMainMenu()`
- **Styling**: Dark background (#222) with consistent padding

### Blackjack Design

#### Game Loop and State Machine

**Round States:**
1. **Inactive**: No round in progress, player can place bet and deal
2. **Active**: Round in progress, turns being processed
3. **Ended**: Round complete, bets settled, ready for new round

**Turn State Machine:**
```
Turn 0 (Human) → Turn 1 (Bot1) → Turn 2 (Bot2) → Turn 3 (Dealer) → End Round
```

**State Transitions:**
- `onDeal()`: Inactive → Active, sets `currentTurnIndex = 0`
- `onHit()`: Active, adds card, checks bust
- `onStand()`: Active, advances turn
- `advanceTurn()`: Increments `currentTurnIndex`, auto-plays bots/dealer
- `endRound()`: Active → Inactive, settles bets, saves high score

#### Bot Thresholds

**Bot 1** (`BotPlayer("AI 1", 1000, 16)`):
- Hit threshold: 16
- Strategy: Hits when hand value < 16
- Conservative approach

**Bot 2** (`BotPlayer("AI 2", 1000, 18)`):
- Hit threshold: 18
- Strategy: Hits when hand value < 18
- More aggressive approach

**Decision Logic:**
```java
public boolean shouldHit() {
    return !busted && !standing && getHandValue() < hitThreshold;
}
```

#### Dealer Logic

**Standard Rules:**
- Hits on hand value < 17
- Stands on hand value > 17
- **Soft 17**: Hits on 17 if ace is counted as 11

**Implementation:**
```java
public boolean shouldHit() {
    int value = getHandValue();
    if (value < 17) return true;
    // Soft 17: hit if value is 17 and has ace
    boolean hasAce = hand.stream().anyMatch(c -> c.getRankChar() == 'A');
    return value == 17 && hasAce;
}
```

#### Save String Format

**Plain Text Format** (before encryption):
```
turnIndex|playerBal,bot1Bal,bot2Bal,dealerBal|playerBet,bot1Bet,bot2Bet|playerHand;bot1Hand;bot2Hand;dealerHand|deckCards
```

**Example Save String**:
```
2|1000,1000,1000,0|200,200,200|2H,QC,7C;6D,7D,TD;3H,9C,4D,4C;TH,5H,AS,5C|AD,8S,9D,KH,QS,2C,8D,4D,6C,3H,7C,5H,5S,9S,TD,JC,3C,4C,4S,8C,6S,QD,JD,3S,AC,TH,TS,9H,5C,JH,3D,2D,2S,8H,QH,4H,QC,AS,6H,KC,6D,9C,AH,7H,7S,JS,KD,2H,7D,TC,5D,KS
```

**Encrypted Format**:
- Entire save string encrypted using `EncryptionUtils.encrypt()`
- Returns Base64-encoded string
- Example: `XyZ123AbC456...` (encrypted, not human-readable)

**Card Encoding**:
- Format: `{rank}{suit}` (2 characters)
- Examples: `AH` (Ace of Hearts), `7C` (7 of Clubs), `TD` (10 of Diamonds)
- Empty hand: `-`

#### How Load Restores Game State

1. **Decryption**: Attempts to decrypt save string (with backward compatibility)
2. **Parsing**: Splits by `|` to get 5 sections
3. **Turn Index**: Restores `currentTurnIndex`
4. **Balances**: Restores all player balances
5. **Bets**: Restores current bets for all players
6. **Hands**: Parses card codes, creates `Card` objects, adds to hands
7. **Deck**: Restores deck state with remaining cards
8. **State Flags**: Recomputes `busted` flags from hand values
9. **Round Active**: Sets `roundActive = true`
10. **UI Update**: Updates all player views
11. **Auto-Continue**: If not player's turn, automatically continues game flow

**Example Load Process:**
```
Encrypted String → Decrypt → Parse Sections → 
Restore Turn Index → Restore Balances → Restore Bets → 
Restore Hands → Restore Deck → Recompute Flags → 
Set Round Active → Update UI → Auto-Continue if needed
```

### Snake Design

#### Tick-Based Movement

**AnimationTimer Implementation:**
```java
timer = new AnimationTimer() {
    long lastUpdate = 0;
    @Override
    public void handle(long now) {
        if (paused) return;
        if (now - lastUpdate < FRAME_DELAY) return; // ~150ms delay
        snake.move();
        // ... collision checks, rendering
        lastUpdate = now;
    }
};
```

**Movement Logic:**
- Head moves in current direction
- Body segments follow previous segment's position
- One cell per frame (20px × 20px cells)
- Board size: 25×25 cells (500×500 pixels)

#### Food Collision Logic

**Detection:**
```java
SnakeSegment head = snake.getSegments().get(0);
boolean foodEaten = (head.getX() == food.getX() && head.getY() == food.getY());
```

**After Eating:**
1. Increment score
2. Grow snake (add segment at tail position)
3. Respawn food (ensure not on snake body)
4. Skip self-collision check this frame (new segment is safe)

#### Self-Collision

**Detection:**
```java
for (int i = 1; i < snake.getSegments().size(); i++) {
    SnakeSegment seg = snake.getSegments().get(i);
    if (seg.getX() == head.getX() && seg.getY() == head.getY()) {
        return true; // Game over
    }
}
```

**Special Case**: After eating food, self-collision check is skipped because the new segment is at the tail position (where head just left).

#### Wall Collision

**Detection:**
```java
if (!board.isInside(head.getX(), head.getY())) {
    return true; // Game over
}
```

**Boundary Check:**
- X: 0 to `BOARD_COLS - 1` (0 to 24)
- Y: 0 to `BOARD_ROWS - 1` (0 to 24)

#### Pause Overlay

**Implementation:**
- Toggle with ESC key
- `PauseOverlay` component displayed when `paused = true`
- Game loop continues but movement paused
- Overlay removed when unpaused

#### Game Over Overlay

**Components:**
- "GAME OVER" text (red, 40pt font)
- Final score display (white, 24pt font)
- "Restart Game" button
- Centered on game board using `StackPane.setAlignment()`

**Functionality:**
- Automatically saves high score
- Restart button calls `restartGame()` method
- Overlay removed on restart

#### Score Writing

**Integration:**
```java
HighScoreController.updateScore(username, score, "snake");
```

**Process:**
1. Game over triggered
2. Final score calculated
3. `HighScoreController.updateScore()` called
4. `FileManager.addHighScore()` adds to list
5. Scores sorted (descending)
6. File saved to `high_scores.txt`

---

## How to Build and Run the Project

### Prerequisites

- **Java Development Kit (JDK)**: Version 11 or higher
- **JavaFX SDK**: Version 21.0.1 or compatible
- **Operating System**: Windows, macOS, or Linux

### Build Instructions

**Compile all source files:**
```bash
javac --module-path /path/to/javafx-sdk-21.0.1/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d out $(find src -name "*.java")
```

**Note**: Replace `/path/to/javafx-sdk-21.0.1/lib` with your actual JavaFX SDK path.

**Alternative build command (if find doesn't work):**
```bash
javac --module-path /path/to/javafx-sdk-21.0.1/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d out src/main/Main.java \
             src/manager/*.java \
             src/blackjack/*.java \
             src/snake/*.java \
             src/storage/*.java \
             src/utils/*.java
```

### Run Instructions

**Execute the application:**
```bash
java --module-path /path/to/javafx-sdk-21.0.1/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp out main.Main
```

**Or run directly from GameManager:**
```bash
java --module-path /path/to/javafx-sdk-21.0.1/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp out manager.GameManager
```

### Running Tests

**Compile test files:**
```bash
javac -d out -cp "out:junit-platform-console-standalone-1.10.0.jar" \
      "junit tests/CardTest.java" \
      "junit tests/EncryptionUtilsTest.java" \
      "junit tests/BotPlayerTest.java"
```

**Run all tests:**
```bash
java -cp "out:junit-platform-console-standalone-1.10.0.jar" \
     org.junit.platform.console.ConsoleLauncher \
     --class-path out \
     --select-class CardTest \
     --select-class EncryptionUtilsTest \
     --select-class BotPlayerTest
```

### Platform-Specific Notes

**macOS/Linux:**
- Use forward slashes in paths
- JavaFX path example: `/Users/username/javafx-sdk-21.0.1/lib`

**Windows:**
- Use backslashes or forward slashes in paths
- JavaFX path example: `C:\javafx-sdk-21.0.1\lib`
- Use `;` instead of `:` in classpath

---

## Usage Guide

### Creating an Account

1. Launch the application
2. Click "Create Account" button on login screen
3. Enter username (minimum 3 characters)
4. Enter password (minimum 4 characters)
5. Confirm password (must match)
6. Click "Create Account"
7. Account created successfully → Redirected to login screen

### Logging In

1. Enter your username
2. Enter your password
3. Click "Login" or press Enter
4. Successful login → Main Menu displayed
5. Invalid credentials → Error message displayed

### Launching Blackjack

1. From Main Menu, click "Blackjack" button (red button on right side)
2. Blackjack main menu appears (with toolbar at top)
3. Options:
   - **Start New Game**: Enter bet amount, click "Deal"
   - **Load Game**: Paste encrypted save state, click "Load Game"

### Playing Blackjack

1. **Place Bet**: Enter bet amount (minimum $10) in bet field
2. **Deal**: Click "Deal" button to start round
3. **Your Turn**:
   - Click "Hit" to take another card
   - Click "Stand" to end your turn
4. **Automatic Play**: Bots and dealer play automatically
5. **Round End**: Bets settled automatically, high score saved
6. **Save Game**: Click "Save Game" to copy encrypted save state
7. **New Round**: Place new bet and click "Deal" again

### Launching Snake

1. From Main Menu, click "Snake" button (green button on right side)
2. Snake game starts immediately (with toolbar at top)
3. Game begins with snake moving right, score at 1000

### Playing Snake

1. **Movement**: Use arrow keys (↑ ↓ ← →) to change direction
2. **Pause**: Press ESC key to pause/unpause
3. **Objective**: Eat food (red circles) to grow and increase score
4. **Avoid**: Walls and your own body
5. **Game Over**: Collision triggers game over screen
6. **Restart**: Click "Restart Game" button to start new game

### Pausing Snake

- Press **ESC** key during gameplay
- Pause overlay appears
- Press **ESC** again to resume
- Game state preserved (snake position, score, food location)

### Saving/Loading Blackjack

**Saving:**
1. During an active round, click "Save Game" button
2. Encrypted save state appears in text area
3. Copy the entire string
4. Save to file or clipboard for later use

**Loading:**
1. From Blackjack main menu, paste save state into "Load Game" text area
2. Click "Load Game" button
3. Game state restored:
   - All balances restored
   - All hands restored
   - Current turn restored
   - Deck state restored
4. If not your turn, game auto-continues automatically

### Viewing High Scores

**On Main Menu:**
- Top 5 scores for each game displayed automatically
- Left side shows Blackjack Top 5
- Left side shows Snake Top 5
- Scores refresh when menu is shown

**Dedicated Screen:**
1. Click "View High Scores" from main menu (if available)
2. Detailed high scores screen appears
3. Click "Refresh Scores" to reload from file
4. Click "Back to Menu" to return

### Logging Out

1. Click "Logout" button (top right of main menu)
2. Confirmation dialog appears
3. Click "OK" to confirm
4. Returned to login screen
5. Session cleared

---

## Demo Video

📹 **Demo Video**: [YouTube Link Here]

*Note: Replace with actual demo video link when available.*

---

## Contribution Breakdown

### 👤 Tanishka — Game Manager + Storage

**Responsibilities:**
- **Game Manager Architecture**: Designed and implemented the core `GameManager` class with centralized scene switching and application lifecycle management
- **Login System**: Built complete `LoginController` with authentication logic, validation, and error handling
- **Account Creation**: Implemented `AccountController` with comprehensive input validation (username length, password matching, duplicate checking)
- **Main Menu Design**: Created `MainMenuController` with split-panel layout featuring integrated high scores display and game selection
- **High Score System**: Developed `HighScoreController` with top 5 display, refresh functionality, and color-coded rankings
- **File Storage System**: Implemented `FileManager` class handling all file I/O operations for:
  - User account persistence (`user_accounts.txt`)
  - High score persistence (`high_scores.txt`)
  - Automatic file creation
  - Data loading and saving
- **User Model**: Created `User` class with encrypted password storage and credential validation
- **High Score Model**: Designed `HighScore` class with timestamp tracking and sorting capabilities
- **Data Format Design**: Established file formats for user accounts and high scores
- **Scene Navigation**: Implemented all scene switching logic and window management

**Key Files:**
- `src/manager/GameManager.java`
- `src/manager/LoginController.java`
- `src/manager/AccountController.java`
- `src/manager/MainMenuController.java`
- `src/manager/HighScoreController.java`
- `src/storage/FileManager.java`
- `src/storage/User.java`
- `src/storage/HighScore.java`

### 👤 Piero — Blackjack Developer

**Responsibilities:**
- **Blackjack Game Logic**: Implemented complete `Blackjack` class with full game flow, turn management, and round sequencing
- **Player Hierarchy**: Designed abstract `Player` class with betting mechanics, hand management, and decision-making interface
- **Human Player**: Created `HumanPlayer` class for manual decision-making via UI
- **AI Bot Players**: Implemented `BotPlayer` class with configurable hit-threshold strategy (AI 1: threshold 16, AI 2: threshold 18)
- **Dealer AI**: Built `Dealer` class with standard casino rules including soft 17 logic
- **Card System**: Designed `Card` class with rank/suit representation, value calculation, and serialization methods
- **Deck Management**: Implemented `Deck` class with shuffle, reset, and card drawing functionality
- **Betting System**: Developed complete betting mechanics with `placeBet()`, `winBet()`, `loseBet()`, and `pushBet()` methods
- **Turn Sequencing**: Implemented `advanceTurn()` with automatic bot and dealer play
- **Initial Blackjack Handling**: Built `checkInitialBlackjack()` to handle individual player blackjacks while allowing game to continue
- **Round Settlement**: Created `settleAgainstDealer()` and `settleAgainstDealerBust()` for bet resolution
- **Save/Load System**: Implemented `SaveGame` class with:
  - Complete game state serialization
  - Encrypted save string generation
  - Full state restoration including hands, balances, bets, deck, and turn index
  - Auto-continue functionality after loading
- **UI Implementation**: Built complete Blackjack UI with:
  - Player view boxes showing hands, balances, bets
  - Turn and status labels
  - Hit/Stand/Deal buttons
  - Save/Load text areas
  - Card rendering with code format
- **Blackjack Controller**: Created `BlackjackController` wrapper for GameManager integration

**Key Files:**
- `src/blackjack/Blackjack.java`
- `src/blackjack/BlackjackController.java`
- `src/blackjack/Player.java`
- `src/blackjack/HumanPlayer.java`
- `src/blackjack/BotPlayer.java`
- `src/blackjack/Dealer.java`
- `src/blackjack/Card.java`
- `src/blackjack/Deck.java`
- `src/blackjack/SaveGame.java`

### 👤 Shresth — Snake + Toolbar + Integration

**Responsibilities:**
- **Snake Game Logic**: Implemented complete `SnakeGame` class with game loop, collision detection, and state management
- **Snake Entity**: Built `Snake` class with direction management, movement logic, and growth functionality
- **Snake Segments**: Created `SnakeSegment` class for individual segment coordinates
- **Food System**: Implemented `Food` class with random spawning and collision avoidance
- **Game Board**: Developed `GameBoard` class with Canvas rendering and boundary collision detection
- **Game Constants**: Defined `SnakeConstants` with board dimensions (25×25), cell size (20px), and frame delay
- **Pause Functionality**: Built `PauseOverlay` component and pause/unpause toggle logic
- **Game Over Screen**: Implemented game over overlay with final score display and restart button
- **Restart Logic**: Created robust `restartGame()` method with:
  - Complete game state reset
  - Key handler recreation and reattachment
  - Focus restoration
  - Timer recreation
- **Collision Detection**: Implemented wall collision, self-collision, and food collision with special handling for food growth
- **Direction Management**: Built direction change logic with opposite-direction prevention and immediate collision checking
- **Score System**: Integrated score tracking with high score saving on game over
- **Animation Loop**: Implemented `AnimationTimer`-based game loop with frame rate control
- **Snake Controller**: Created `SnakeController` wrapper for GameManager integration
- **Toolbar Component**: Designed and implemented reusable `Toolbar` class:
  - "Main Menu" button functionality
  - Consistent styling across all game screens
  - Integration with both Blackjack and Snake games
  - Proper positioning using BorderPane layout
- **Game Integration**: Integrated Snake game with GameManager:
  - Scene wrapping with Toolbar
  - Key event handler transfer
  - Focus management for keyboard input
  - Proper dimension calculation
- **Encryption Integration**: Integrated `EncryptionUtils` into save/load systems and password storage

**Key Files:**
- `src/snake/SnakeGame.java`
- `src/snake/SnakeController.java`
- `src/snake/Snake.java`
- `src/snake/SnakeSegment.java`
- `src/snake/Food.java`
- `src/snake/GameBoard.java`
- `src/snake/SnakeConstants.java`
- `src/snake/PauseOverlay.java`
- `src/manager/Toolbar.java`
- `src/utils/EncryptionUtils.java`

---

## Future Improvements

Based on the current implementation, the following enhancements could be considered:

### Blackjack Enhancements
- **Advanced AI Strategies**: Implement card counting or more sophisticated bot decision-making
- **Multiple Deck Support**: Add option for 6-deck or 8-deck games
- **Side Bets**: Implement insurance, split, and double-down options
- **Statistics Tracking**: Track win/loss ratios, average bet, longest winning streak
- **Tournament Mode**: Multi-round tournaments with leaderboards

### Snake Enhancements
- **Difficulty Modes**: Adjustable speed settings (Easy, Medium, Hard)
- **Power-ups**: Temporary speed boost, invincibility, score multipliers
- **Obstacles**: Moving or static obstacles on the board
- **Multiplayer**: Local multiplayer with split-screen or network play
- **Custom Themes**: Different color schemes and board designs

### General Improvements
- **Sound Effects**: Audio feedback for game events (card deals, food eaten, game over)
- **Music**: Background music with volume control
- **Theme Customization**: Dark/light mode, color scheme selection
- **Statistics Dashboard**: Comprehensive player statistics across all games
- **Achievement System**: Unlockable achievements for milestones
- **Profile Pictures**: User avatar selection
- **Game History**: View past game results and replays
- **Export/Import**: Backup and restore game data
- **Network Features**: Online leaderboards, multiplayer support

### Technical Improvements
- **Database Integration**: Replace file-based storage with SQLite or PostgreSQL
- **Configuration File**: External config for game settings
- **Logging System**: Comprehensive logging for debugging and analytics
- **Error Handling**: Enhanced error messages and recovery mechanisms
- **Performance Optimization**: Optimize rendering and game loop efficiency
- **Mobile Support**: Port to Android/iOS using JavaFX Mobile

---

## License

This project is developed for educational purposes as part of CS 151 - Object-Oriented Design and Programming (Fall 2024) at [University Name], under Professor Telvin Zhong.

**MIT License**

Copyright (c) 2024 CS151 Project 2 Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---

## Acknowledgments

- **Professor Telvin Zhong** - CS151 Fall 2024 Project 2 specifications
- **JavaFX Team** - For the excellent JavaFX framework
- **JUnit Team** - For the testing framework

---

*This project was developed as part of CS 151 - Object-Oriented Design and Programming coursework.*
