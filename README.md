# The Magic Run 🧙‍♂️

## Description
The Magic Run is an engaging Android obstacle avoidance game where players control a magician who must dodge evil witches falling from above. Test your reflexes and magical abilities as you navigate through increasingly challenging patterns of obstacles!

## Game Features
- Simple yet addictive gameplay mechanics
- Intuitive controls with left and right movement
- Health system with three lives
- Dynamic obstacle generation
- Vibration feedback on collisions
- Magical themed graphics and UI

## How to Play
1. **Start**: Launch the game to begin your magical adventure
2. **Controls**: 
   - Tap the left arrow button to move left
   - Tap the right arrow button to move right
3. **Objective**: Avoid colliding with the falling witch obstacles
4. **Lives**: 
   - You start with 3 lives (represented by hearts)
   - Each collision with a witch reduces your lives by 1
   - The game ends when you lose all lives

## Game Mechanics
- **Player Character**: A magician fixed at the bottom of the screen
- **Obstacles**: Evil witches that fall from the top of the screen
- **Movement**: Horizontal movement across three lanes
- **Collision System**: 
  - Includes a brief invulnerability period after each hit
  - Triggers vibration feedback
  - Displays a warning message
- **Obstacle Generation**:
  - 60% chance of new obstacle generation
  - Guarantees at least one safe path is always available

## Technical Features
- Built for Android using Android Studio
- Implements custom collision detection system
- Uses Timer for obstacle movement
- Includes vibration feedback system
- Contains game over screen with status message
- Automatic activity transition on game end

## Requirements
- Android device or emulator
- Minimum SDK version: As specified in build.gradle
- Vibration permission enabled

## Installation
1. Clone the repository
2. Open the project in Android Studio
3. Build and run on your Android device or emulator

## Game Over
The game ends when all three lives are lost, displaying a "Magic Failed!" message and transitioning to the game over screen after 5 seconds.

## Development
The game is structured using several key components:
- MainActivity: Main game logic and UI
- GameOverActivity: Handles game end state
- SignalManager: Manages vibrations and toast messages
- Custom layouts for both game and game over screens

## Credits
Created using Android Studio and Kotlin
