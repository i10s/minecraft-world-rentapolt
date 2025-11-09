# Rentapolt: Ion's World

**Version 1.0.0** - A Fabric mod for Minecraft 1.20.1 that transforms the Overworld into Ion's epic playground with fully explorable NYC-style megacities, custom biomes, powerful weapons, armor sets, boss battles, and endless chaos.

## 🎉 What's New in v1.0.0

### 🗽 Iconic NYC Skyscrapers (NEW!)
The city now features **5 authentic New York landmarks** (5% spawn chance):
- **Empire State Building** - Art Deco masterpiece with stepped crown (380 blocks + antenna)
- **One World Trade Center** - Modern glass tower with chamfered edges (541 blocks with spire)
- **Chrysler Building** - Iconic steel crown with triangular arches (319 blocks)
- **Flatiron Building** - Triangular "wedge" shape (87 blocks)
- **432 Park Avenue** - Ultra-slender residential tower (426 blocks)

All iconic buildings include complete interiors with floors, elevators, and spiral staircases!

### � Super Realistic NYC Details
Every city block now features authentic New York street life:
- **🚕 Yellow taxis** (70% spawn rate)
- **🌭 Hot dog/pretzel vendor carts** (40%)
- **🪜 Fire escape stairs** on buildings (80% on tall buildings)
- **❄️ AC units** in windows (buildings >20 blocks)
- **💧 Water towers** on rooftops (60% on tall buildings)
- **🗑️ Trash piles and dumpsters** (60%)
- **📰 Newspaper boxes** (50%)
- **☂️ Store awnings** with colored fabric (70% on shops)
- **🎨 Graffiti** on walls (30%)
- **🚚 Delivery trucks** (30%)
- **🏗️ Construction scaffolding** (10% on tall buildings)

### �🏙️ Fully Explorable Buildings
Every building in Rentapolt City now has **complete interiors** with:
- **Multi-floor layouts** - Proper floors every 3-4 blocks
- **Glass entrance doors** - Welcoming entryways with frames
- **Internal spiral staircases** - Explore from ground to rooftop
- **Windows** - Natural lighting on every floor
- **Elevator shafts** - In mega buildings over 40 blocks tall
- **Themed furniture** - Each building type has unique decorations

### 🏢 6 Different Building Types
- **🛍️ Shops** (Cyan) - Display counters, chests, mannequins
- **🍴 Restaurants** (Red) - Tables, chairs, kitchen areas
- **🏦 Banks** (Yellow) - Teller counters, vault doors
- **🏨 Hotels** (Purple) - Reception desk, rooms with beds
- **🏢 Offices** (Blue) - Desks, computers, bookshelves
- **🏠 Apartments** (Green) - Beds, kitchens, living spaces

### 🚇 Urban Infrastructure
- Traffic lights, crosswalks, fire hydrants
- Benches, trash cans, mailboxes, parking meters
- Street lamps (90% coverage), urban trees (30%)
- **Subway entrances** (10% spawn) - Descend to underground platforms with dual rail tracks

## Highlights

- **New Overworld** – The default dimension is replaced with Rentapolt biomes: neon City, peaceful Prairie, toxic Mutant Zone, and underground Secret Bunker fields.
- **NYC-Style Megacity** – Epic skyscrapers **up to 80 blocks tall** with complete interiors, rooftop pools, helipads, antenna spires with beacons, colorful LED facade lights, and urban atmosphere with rain and smoke particles.
- **6 Building Types** – Shops, restaurants, banks, hotels, offices, and luxury apartments, each with unique interiors and themed furniture.
- **Urban Infrastructure** – Traffic lights, crosswalks, subway entrances, street lamps, fire hydrants, benches, and more.
- **Custom structures** – Procedurally generated roads, multi-story buildings (5-80 blocks tall), underground bunker networks, subway stations, ruined structures, and rare floating islands with tiered loot chests.
- **Armory upgrades** – Six unique weapons and five full armor sets with gameplay abilities such as flight, invisibility, burn immunity, lightning strikes, and teleportation.
- **Creatures and allies** – Hostile mutants (creepers, zombies, fire golems, plasma beasts, serpents) plus peaceful lions, elephants, phoenixes, and griffins with aura buffs.
- **Epic Boss Battles** – Face the Mega Mutant (500 HP), Ancient Phoenix (flying boss), and Shadow King (teleporting assassin) for legendary rewards.
- **Advanced AI** – Lions hunt in packs, phoenixes heal allies, griffins dive-bomb, mutants flank and coordinate attacks.
- **Tiered Loot System** – 5-tier loot (Common → Legendary) with Chaos Coins currency and 7 unique artifacts.
- **Blocks & tech** – Energy blocks, random Teleporters, Explosive traps, and glowing city décor integrated with crafting recipes and loot.
- **Audio & ambience** – Custom sound events and music cues for portal ignitions, lightning slams, plasma bursts, and mutant biomes.

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
