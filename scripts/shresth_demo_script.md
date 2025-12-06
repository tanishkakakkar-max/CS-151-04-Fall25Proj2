# Snake Game Demo Script - Shresth (4-5 Minute Version)

## Introduction

Hi, I'm Shresth, and I developed the entire Snake game for this project. I built six core classes that handle the game loop, rendering, collision detection, pause and game-over systems, and full integration with the Game Manager.

---

## Core Classes Overview

Let me start with the supporting classes. **SnakeConstants** centralizes configuration - 25 by 25 board, 20-pixel cells, and frame delay for game speed (lines 1-13). **SnakeController** wraps the game for clean integration with GameManager (lines 1-19). **SnakeSegment** represents individual body parts with x and y coordinates (lines 1-22). **Food** manages random spawning within board boundaries (lines 1-22).

---

## Snake.java (lines 1-64)

**Snake.java** contains the core movement logic. The snake maintains an ArrayList of segments and tracks direction (lines 7-8). (Lines 10-12) define a Direction enum - UP, DOWN, LEFT, RIGHT - for type safety.

The **move method** (lines 40-58) is the heart of snake movement. (Lines 41-44) handle body segments - each segment takes the position of the segment ahead, creating the classic movement pattern. (Lines 50-55) update the head's position based on direction. The **isOpposite method** (lines 32-37) prevents instant direction reversal, which would cause immediate death. The **grow method** (lines 60-63) adds a new segment at the tail when food is eaten.

---

## GameBoard.java (lines 1-45)

**GameBoard** handles all rendering and boundary checking. It stores a Canvas with calculated dimensions - 25 rows times 20 pixels equals 500 pixels (lines 8-18). The **render method** (lines 24-37) draws everything each frame - clears the canvas black, draws food as a red circle, and draws each snake segment as a green rectangle. The **isInside method** (lines 42-44) checks if coordinates are within board boundaries for wall collision detection.

---

## SnakeGame.java - Game Loop (lines 84-119)

**SnakeGame.java** is the main class that orchestrates everything. The game loop uses JavaFX's AnimationTimer starting at (line 84). (Line 89) checks if paused and skips updates. (Line 90) applies frame delay to control speed - updating every 150 million nanoseconds.

(Line 92) moves the snake one cell. (Lines 95-96) check if the head reaches food. (Lines 98-107) handle food collision - increment score, grow the snake, and spawn new food. The do-while loop ensures food never spawns on the snake (lines 104-106). (Lines 109-114) check for game over conditions. I pass foodEaten as a parameter to skip self-collision check when food is just eaten, since the new segment is at the tail which is safe.

---

## SnakeGame.java - Collision Detection (lines 124-183)

For collision detection, **isFoodOnSnake** (lines 124-131) prevents unfair food placement. **wouldCauseImmediateCollision** (lines 133-161) prevents the snake from turning into its own neck. **checkGameOver** (lines 162-183) handles two collision types - (lines 165-169) check wall collision, and (lines 171-180) check self-collision. However, if we just ate food, (line 173) skips self-collision because the new segment is at the tail position, which is safe.

---

## SnakeGame.java - Pause & Game Over (lines 184-222)

The **pause system** is in togglePause (lines 184-193). (Line 185) toggles the paused flag. (Lines 187-189) add a PauseOverlay with semi-transparent background when pausing. The game loop checks the paused flag (line 89), so when paused, no updates occur but the UI remains visible.

The **game over system** is in showGameOverOverlay (lines 195-222). (Lines 196-200) automatically save the high score to HighScoreController. (Lines 202-221) create a VBox overlay with "GAME OVER" text, final score display, and a restart button that calls restartGame.

---

## SnakeGame.java - Restart Functionality (lines 224-351)

The **restart functionality** is complex. restartGame starts at (line 224). (Lines 226-231) remove overlays and stop the old timer. (Lines 233-245) reset game state and respawn food.

(Lines 247-256) are critical - we remove all existing key handlers to prevent conflicts. (Lines 258-274) recreate the key event handler - this is essential because lambdas capture variables by reference, so the handler must reference the new snake instance, not the old one.

(Lines 276-286) attach the new handler to all necessary nodes. (Lines 288-320) create a completely new AnimationTimer - we can't reuse the old one. (Lines 322-350) request focus multiple times using Platform.runLater to ensure keyboard input works after restart, especially when wrapped in a BorderPane with a toolbar.

---

## Toolbar & Integration (Toolbar.java lines 1-22, GameManager.java lines 108-153)

**Toolbar.java** is a reusable HBox component with a "Main Menu" button that takes a Runnable callback (line 11). It provides consistent navigation across all game screens.

In **GameManager.launchSnake** (lines 108-153), we integrate the Snake game with the toolbar. (Lines 122-125) wrap the game in a BorderPane with the Toolbar at the top. (Lines 138-146) transfer key event handlers to both the new scene and game root to ensure keyboard input works even when wrapped. (Line 152) requests focus so arrow keys work immediately. This integration ensures the Snake game works seamlessly with the toolbar and automatically saves high scores to the shared system.

---

## Summary

To summarize, I developed the complete Snake game with six modular classes, a smooth game loop with frame rate control, robust collision detection, pause and game over systems, and full integration with the Game Manager and toolbar. The restart functionality required careful state management to ensure handlers reference the correct snake instance and focus is properly maintained.

Thank you for your attention, and I'm happy to answer any questions.
