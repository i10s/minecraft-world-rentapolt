# Rentapolt: Ion's World

Rentapolt is a Fabric mod for Minecraft 1.20.1 that replaces the Overworld with Ion's chaotic playground. Custom biomes, mobs, armor sets, weapons, and structures turn survival into an overpowered adventure built for Ion and his dad.

## Highlights

- **New Overworld** – the default dimension is replaced with Rentapolt biomes like the neon City, peaceful Prairie, toxic Mutant Zone, and underground Secret Bunker fields.
- **Custom structures** – procedurally generated roads, multi-story buildings, underground bunker networks, ruined structures, and rare floating islands with tiered loot chests.
- **Armory upgrades** – six unique weapons and five full armor sets with gameplay abilities such as flight, invisibility, burn immunity, lightning strikes, and teleportation.
- **Creatures and allies** – hostile mutants (creepers, zombies, fire golems, plasma beasts, serpents) plus peaceful lions, elephants, phoenixes, and griffins with aura buffs.
- **Epic Boss Battles** – face the Mega Mutant (500 HP), Ancient Phoenix (flying boss), and Shadow King (teleporting assassin) for legendary rewards.
- **Advanced AI** – lions hunt in packs, phoenixes heal allies, griffins dive-bomb, mutants flank and coordinate attacks.
- **Tiered Loot System** – 5-tier loot (Common → Legendary) with Chaos Coins currency and 7 unique artifacts.
- **Blocks & tech** – Energy blocks, random Teleporters, Explosive traps, and glowing city décor integrated with crafting recipes and loot.
- **Audio & ambience** – custom sound events and music cues for portal ignitions, lightning slams, plasma bursts, and mutant biomes.

## Building & running

### Prerequisites

1. Install a Java 17 JDK
2. **Linux users**: Install `haveged` to prevent slow startup:

   ```bash
   sudo apt-get install haveged
   sudo systemctl enable --now haveged
   ```

### Build and run

From the repository root:

```bash
./gradlew genSources
./gradlew runClient
```

Launch a new world; the Overworld will use the Rentapolt generator automatically.

### Troubleshooting

**"Generating keypair" hangs for a long time:**

This happens on systems with low entropy. The `build.gradle` includes a fix (`java.security.egd=file:/dev/urandom`), but if it still hangs:

1. **Install haveged** (see Prerequisites above)
2. **Wait it out** - It will complete in 15-30 minutes
3. **Generate activity** - Move your mouse, type, browse files to increase system entropy

**Docker deployment:**

For running a dedicated server in Docker, see [docker/README.md](docker/README.md)

## Connecting to the Server

The Docker server runs on your local machine. To connect from Minecraft:

### 1. Find Your Server IP

```bash
# On Linux/Mac - find your local network IP
ip addr show | grep "inet " | grep -v 127.0.0.1
# Or use:
hostname -I

# On Windows (Command Prompt)
ipconfig
# Look for "IPv4 Address" under your active network adapter
```

Your IP will look like `192.168.x.x` or `10.0.x.x`

### 2. Connect from Minecraft Client

1. **Launch Minecraft 1.20.1** with Fabric Loader installed
2. Click **Multiplayer**
3. Click **Add Server**
4. Enter:
   - **Server Name**: Ion's World (or whatever you like)
   - **Server Address**: `<YOUR_IP>:25565` (e.g., `192.168.1.100:25565`)
   - Or use `localhost:25565` if playing on the same machine
5. Click **Done** and connect!

### 3. Server Management

```bash
# Check if server is running
cd docker && make logs-live

# Restart server
cd docker && make restart

# Stop server
cd docker && make stop

# Deploy updated mod
cd docker && make deploy
```

**Note:** The server is configured in `docker/server.properties`. Default settings:
- Port: 25565
- Max players: 20
- Difficulty: Hard
- Gamemode: Survival

## Project layout

- `src/main/java/com/rentapolt` – mod entry point, blocks, items, entities, armor logic, and worldgen builders.
- `src/main/resources/assets/rentapolt` – language keys, models, textures, placeholder art, and sound definitions.
- `src/main/resources/data/rentapolt` – recipes, loot tables, biomes, structures, and tag data.
- `src/main/resources/data/minecraft/dimension/overworld.json` – overrides vanilla Overworld to point at Rentapolt biomes/features.

Have fun storming Ion's world! Contributions and tweaks are welcome—just keep the chaos cranked to 11.
