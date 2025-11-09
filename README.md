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

**Bug fixes and troubleshooting:**

For known issues and their solutions, see [BUGFIXES.md](BUGFIXES.md)

## Connecting to the Server

The Docker server runs on your local machine. To connect from Minecraft, you need to set up the client with Fabric Loader and the required mods.

**Important:** Always build the latest version of the mod before connecting:

```bash
./gradlew build
```

### 1. Quick Client Setup (Linux/Mac)

Run the automated setup script:

```bash
./setup-client.sh
```

This script will:

- Copy the Rentapolt mod to `~/.minecraft/mods/`
- Download Fabric API
- Show you the next steps for Fabric Loader installation

### 2. Manual Client Setup

#### Step 1: Install Fabric Loader

**If using vanilla Minecraft Launcher:**

1. Download Fabric Installer from <https://fabricmc.net/use/installer/>
2. Run the installer:

   ```bash
   java -jar fabric-installer.jar
   ```

3. Select **Minecraft version 1.20.1**, choose **Client**, and click **Install**

**If using Minecraft Launcher via Snap (Linux):**

The launcher is installed at `~/snap/mc-installer/638/.minecraft/`. Install Fabric using CLI:

```bash
# Download Fabric installer
wget -O /tmp/fabric-installer.jar https://maven.fabricmc.net/net/fabricmc/fabric-installer/1.0.1/fabric-installer-1.0.1.jar

# Install Fabric for Minecraft 1.20.1
java -jar /tmp/fabric-installer.jar client -dir ~/snap/mc-installer/638/.minecraft -mcversion 1.20.1 -loader 0.17.3
```

#### Step 2: Install Required Mods

**For vanilla launcher (standard `~/.minecraft/`):**

```bash
# Create mods directory
mkdir -p ~/.minecraft/mods

# Copy Rentapolt mod
cp build/libs/rentapolt-0.1.0.jar ~/.minecraft/mods/

# Download Fabric API
cd ~/.minecraft/mods
wget -O fabric-api.jar "https://cdn.modrinth.com/data/P7dR8mSH/versions/P7uGFii0/fabric-api-0.92.2%2B1.20.1.jar"
```

**For Snap launcher:**

```bash
# Create mods directory
mkdir -p ~/snap/mc-installer/638/.minecraft/mods

# Copy Rentapolt mod
cp build/libs/rentapolt-0.1.0.jar ~/snap/mc-installer/638/.minecraft/mods/

# Download Fabric API
cd ~/snap/mc-installer/638/.minecraft/mods
wget -O fabric-api.jar "https://cdn.modrinth.com/data/P7dR8mSH/versions/P7uGFii0/fabric-api-0.92.2%2B1.20.1.jar"
```

#### Step 3: Launch Minecraft

1. Open **Minecraft Launcher**
2. Select profile: **fabric-loader-0.17.3-1.20.1** (or similar)
3. Click **Play**

#### Step 4: Find Your Server IP

```bash
# On Linux/Mac - find your local network IP
hostname -I
# Or:
ip addr show | grep "inet " | grep -v 127.0.0.1

# On Windows (Command Prompt)
ipconfig
# Look for "IPv4 Address" under your active network adapter
```

Your IP will look like `192.168.x.x` or `10.0.x.x`

#### Step 5: Connect to Server

1. In Minecraft, click **Multiplayer**
2. Click **Add Server**
3. Enter:
   - **Server Name**: Ion's World (or whatever you like)
   - **Server Address**: `localhost:25565` (same machine) or `<YOUR_IP>:25565` (from another computer)
4. Click **Done** and **Join Server**!

### 3. Verify Client Installation

Check that you have both required mods installed:

```bash
# For vanilla launcher
ls -lh ~/.minecraft/mods/

# For Snap launcher
ls -lh ~/snap/mc-installer/638/.minecraft/mods/

# You should see:
# fabric-api.jar (~2.1M)
# rentapolt-0.1.0.jar (~156K)
```

### 4. Server Management

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
