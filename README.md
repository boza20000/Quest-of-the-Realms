# Quest of the Realms 🗺️⚔️

A feature-rich **Java-based RPG game** with single-player and multiplayer support. Embark on epic quests, battle enemies, manage inventory, interact with NPCs, and experience a dynamic world filled with adventure.

## 🎮 Features

### Core Gameplay
- **Single-Player & Multiplayer Modes**: Play solo or connect to a server for shared adventures
- **Character Classes**: Choose from different character types (Warrior, Mage, Ranger, etc.) with unique abilities
- **Dynamic World Map**: Procedurally generated tiles with diverse terrains and locations
- **Real-Time Combat System**: Battle enemies with strategic spell and ability usage
- **Quest & Mission System**: Complete missions from NPCs to progress the story
- **Inventory Management**: Collect, equip, and trade items with merchants
- **Save/Load System**: Persistent game saves for both single-player and server-based games

### Advanced Features
- **NPC Interactions**: Talk to village NPCs, merchants, and quest-givers
- **Loot System**: Enemies drop random items when defeated
- **Spell & Ability System**: Cast spells and use character abilities in combat
- **Multiplayer Synchronization**: Real-time game state synchronization across players
- **Localization Support**: Multi-language message bundle system
- **Comprehensive Logging**: Server-side logging for debugging and monitoring

## 🛠️ Tech Stack

- **Language**: Java 24
- **Build Tool**: Maven
- **Data Persistence**: Jackson (JSON serialization)
- **Testing**: JUnit 5, Mockito
- **Networking**: Socket-based client-server architecture
- **Logging**: Custom server logging system

## 📁 Project Structure

```
Quest-of-the-Realms/
├── src/
│   ├── main/java/com/questoftherealm/
│   │   ├── characters/          # Player and character classes
│   │   ├── client/              # Client-side networking logic
│   │   ├── commands/            # Command execution system
│   │   ├── enemyEntities/       # Enemy types and combat logic
│   │   ├── expeditions/         # Quest and expedition system
│   │   ├── friendlyEntities/    # NPCs and friendly entities
│   │   ├── game/                # Core game logic and state management
│   │   ├── interaction/         # User interaction and console controller
│   │   ├── items/               # Item system and inventory
│   │   ├── localization/        # Message bundles and translations
│   │   ├── map/                 # World map and tile system
│   │   ├── server/              # Server networking and request handling
│   │   ├── spells/              # Spell and ability definitions
│   │   └── Main.java            # Application entry point
│   ├── resources/
│   │   ├── messages.properties  # Localization strings
│   │   ├── items.json           # Item definitions
│   │   └── map.json             # World map configuration
│   └── test/                    # Unit and integration tests
├── pom.xml                      # Maven configuration
└── README.md                    # This file
```

## 🚀 Getting Started

### Prerequisites
- **Java 24** or higher
- **Maven 3.6+**
- Git

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/Quest-of-the-Realms.git
   cd Quest-of-the-Realms
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:

   **Single-Player Mode**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.questoftherealm.Main"
   ```

   **Server Mode**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.questoftherealm.server.Server"
   ```

   **Client Connection**:
   After starting the server, run the client and select "Connect to server" option.

## 📖 How to Play

### Game Modes

**1. Single-Player Mode**
- Create a new character and embark on solo adventures
- Load previously saved games
- Experience the full story offline

**2. Multiplayer Mode**
- Connect to a game server (default: localhost:2020)
- Play with other connected players
- Share the same world and compete/cooperate

### Basic Commands

```
look          - Examine the current tile
move [dir]    - Move in a direction (north, south, east, west)
attack [npc]  - Engage in combat with an enemy
talk [npc]    - Interact with NPCs
inventory     - Check your items and equipment
use [item]    - Use an item from inventory
equip [item]  - Equip an item
help          - Display available commands
save          - Save your progress (single-player)
load          - Load a previous save (single-player)
quit          - Exit the game
```

### Character Classes

| Class | Strengths | Abilities |
|-------|-----------|-----------|
| **Warrior** | High HP, melee damage | War Cry, Block |
| **Mage** | Spell casting, AoE attacks | Fireball, Heal |
| **Ranger** | Agility, ranged attacks | Quick Shot, Dodge |
| **Paladin** | Balanced stats, support | Smite, Heal Party |

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=GameLoopTest
```

### Test Structure

Tests follow the **Given-When-Then** convention for readability:
- **Given**: Setup initial test state
- **When**: Execute the action being tested
- **Then**: Verify the expected outcome

Example:
```java
@Test
void whenPlayerMovesNorth_thenPositionIsUpdated() {
    // Given
    Player player = createTestPlayer();
    
    // When
    player.move("north", gameState);
    
    // Then
    assertEquals(expectedX, player.getX());
    assertEquals(expectedY, player.getY());
}
```

## 🔧 Configuration

### Server Configuration
Edit `GameConstants.java` to modify:
- Maximum players per session
- Player spawn location
- Inventory size limits
- Game difficulty settings

### Localization
Add or modify translations in `src/main/resources/messages.properties`:
```properties
game.welcome=Welcome to Quest of the Realms!
player.stats=Health: {0} | Mana: {1} | Experience: {2}
```

## 🐛 Known Issues & Fixes

- **Issue**: Console output blocking in multiplayer mode
  - **Status**: ✅ Fixed - See `CLIENT_BUFFERING_FIX.md`

- **Issue**: Enemy death synchronization lag
  - **Status**: ✅ Fixed - See `ENEMY_DEATH_SYNC_FIX.md`

- **Issue**: Server not stopping gracefully at max players
  - **Status**: ✅ Fixed - See `SERVER_MAX_PLAYERS_SHUTDOWN_FIX.md`

See `.md` files in root directory for detailed fix documentation.

## 📊 Architecture Highlights

### Game Loop
The `GameLoop` class manages the core game cycle:
1. **Input Processing**: Read and validate player commands
2. **Command Execution**: Execute the appropriate game command
3. **State Update**: Update game state based on command results
4. **Output Display**: Render updated game state to player

### Thread Safety
- All shared game state is properly synchronized
- Concurrent access to tile content, inventory, and player lists
- Thread-safe logging for multi-threaded environment

### State Management
- `GameState`: Centralized game state container
- `GameServices`: Provides input/output, random generation, logging
- `Player`: Individual player state and statistics

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Create a feature branch: `git checkout -b feature/AmazingFeature`
2. Commit changes: `git commit -m 'Add AmazingFeature'`
3. Push to branch: `git push origin feature/AmazingFeature`
4. Open a Pull Request

### Code Standards
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Write unit tests for new features
- Use synchronized blocks for concurrent access

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🎯 Roadmap

- [ ] Enhanced AI for enemies
- [ ] Dungeons and boss battles
- [ ] Skill trees and character progression
- [ ] PvP arena mode
- [ ] Trading system between players
- [ ] Mobile client support
- [ ] Web-based dashboard for server monitoring
- [ ] Mod support and plugin system

## 💬 Feedback & Support

For bugs, feature requests, or questions:
- Open an [Issue](https://github.com/yourusername/Quest-of-the-Realms/issues)
- Create a [Discussion](https://github.com/yourusername/Quest-of-the-Realms/discussions)

## 📸 Screenshots

(Add screenshots of gameplay here)

---

**Happy Questing! May your adventures in the Realms be legendary.** ⚔️🗺️✨

