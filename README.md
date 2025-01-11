# Magic Run 🎮✨

A magical obstacle avoidance game where players control a magician to dodge evil witches and collect magical hats.

## Description

Magic Run is an Android game where players control a magician character who must navigate through a field of obstacles. The game features both harmful obstacles (witches) that must be avoided and beneficial ones (magic hats) that should be collected for points.

## Features

### Game Modes
- **Button Controls**: Traditional control scheme using left/right buttons
- **Sensor Controls**: Tilt your device to move the magician
  - Tilt left/right to move
  - Tilt forward/backward to adjust game speed

### Difficulty Levels
- **Easy**: Slower obstacle movement for beginners
- **Hard**: Faster-paced gameplay for experienced players

### Gameplay Elements
- Three lives represented by magic wands
- Score tracking system
- Obstacle variety:
  - Evil witches (harmful) - Cost one life on collision
  - Magic hats (beneficial) - Award 10 points when collected
- Dynamic speed adjustment in sensor mode

### Additional Features
- High score tracking
- Location-based score recording
- Interactive score map
- Sound effects and vibration feedback

## Technical Specifications

### Requirements
- Android SDK 26 or higher (Android 8.0+)
- Google Play Services for Maps
- Location permissions for score tracking

### Dependencies
```gradle
implementation 'androidx.core:core-ktx'
implementation 'androidx.appcompat:appcompat'
implementation 'com.google.android.material:material'
implementation 'com.google.android.gms:play-services-maps:18.2.0'
implementation 'com.google.android.gms:play-services-location:21.1.0'
implementation 'com.google.code.gson:gson:2.10.1'
implementation 'androidx.recyclerview:recyclerview:1.3.0'
```

### Key Components
1. **Game Logic**
   - `GameBoard`: Manages the game grid and obstacle placement
   - `PlayerManager`: Handles player movement and position
   - `ObstacleManager`: Controls obstacle generation and movement
   - `GameTimer`: Manages game speed and updates
   - `GameManager`: Controls core game mechanics including:
     - Life management
     - Score tracking
     - Collision detection
     - Sound effects
     - Vibration feedback
     - Game over conditions

2. **UI Components**
   - Custom layouts for game board
   - Material Design components
   - Dynamic score display
   - Life indicator system

3. **Data Management**
   - SharedPreferences for score storage
   - Location tracking for score mapping
   - High score leaderboard system

## First Launch

When you first launch the application, you'll be prompted to grant location permissions. This is necessary for:
- Recording the location of your high scores
- Displaying scores on the interactive map
- Enhanced leaderboard functionality

You can choose to:
- Allow location access for full game features
- Deny location access (some features will be limited)

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio
3. Add your Google Maps API key in the AndroidManifest.xml:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY"/>
   ```
4. Build and run the project

## Permissions Required
- `VIBRATE`: For haptic feedback
- `ACCESS_FINE_LOCATION`: For score location tracking
- `ACCESS_COARSE_LOCATION`: For approximate location data

## Game Controls

### Button Mode
- Tap left button to move left
- Tap right button to move right

### Sensor Mode
- Tilt device left to move left
- Tilt device right to move right
- Tilt forward to increase speed
- Tilt backward to decrease speed

## Development Notes

- Built using Kotlin
- Follows MVVM architecture pattern
- Uses Material Design components
- Implements fragment-based UI for score display
- Utilizes Google Maps API for location features

## Credits

Developed as a learning project using Android Studio and Kotlin.

## License

This project is open for educational purposes.
