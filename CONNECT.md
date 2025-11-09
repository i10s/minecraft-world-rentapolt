# Connecting to Rentapolt Server

Complete guide to set up your Minecraft client and connect to the Rentapolt server.

## 🎮 Server Status

✅ **Server is RUNNING!**

- Address: `192.168.1.111:25565` (or `localhost:25565` from this machine)
- Version: Minecraft 1.20.1 with Fabric Loader 0.17.3
- Port: 25565

## Quick Setup (Automated)

If you're on Linux/Mac, use the automated setup script:

```bash
cd /path/to/minecraft-world-rentapolt
./setup-client.sh
```

This will:

- Install Rentapolt mod to your mods folder
- Download Fabric API
- Show you installation status and next steps

Then skip to **Step 3: Launch & Connect** below.

## Manual Setup

### Step 1: Install Fabric Loader

You need Fabric Loader to run mods. The installation method depends on your launcher type.

#### Option A: Vanilla Minecraft Launcher

1. Download Fabric Installer from <https://fabricmc.net/use/installer/>
2. Run the installer:

   ```bash
   java -jar fabric-installer.jar
   ```

3. In the installer window:
   - Select **Minecraft Version: 1.20.1**
   - Keep **Loader Version** as latest (0.17.3 or higher)
   - Choose **Install Client**
   - Click **Install**

4. You'll see "Successfully installed" message

#### Option B: Snap Launcher (Linux)

If you installed Minecraft via Snap, the launcher is at `~/snap/mc-installer/638/.minecraft/`. Install Fabric using command line:

```bash
# Download Fabric installer
wget -O /tmp/fabric-installer.jar https://maven.fabricmc.net/net/fabricmc/fabric-installer/1.0.1/fabric-installer-1.0.1.jar

# Install Fabric for Minecraft 1.20.1
java -jar /tmp/fabric-installer.jar client -dir ~/snap/mc-installer/638/.minecraft -mcversion 1.20.1 -loader 0.17.3
```

You should see output ending with "Done" and "Creating profile".

### Step 2: Install Required Mods

You need **two mods** installed:

1. **Rentapolt** (the custom mod)
2. **Fabric API** (dependency)

#### Option A: For Vanilla Launcher

```bash
# Create mods directory
mkdir -p ~/.minecraft/mods

# Build and copy Rentapolt mod (from project directory)
cd /path/to/minecraft-world-rentapolt
./gradlew build
cp build/libs/rentapolt-0.1.0.jar ~/.minecraft/mods/

# Download Fabric API
cd ~/.minecraft/mods
wget -O fabric-api.jar "https://cdn.modrinth.com/data/P7dR8mSH/versions/P7uGFii0/fabric-api-0.92.2%2B1.20.1.jar"
```

#### Option B: For Snap Launcher

```bash
# Create mods directory
mkdir -p ~/snap/mc-installer/638/.minecraft/mods

# Build and copy Rentapolt mod
cd /path/to/minecraft-world-rentapolt
./gradlew build
cp build/libs/rentapolt-0.1.0.jar ~/snap/mc-installer/638/.minecraft/mods/

# Download Fabric API
cd ~/snap/mc-installer/638/.minecraft/mods
wget -O fabric-api.jar "https://cdn.modrinth.com/data/P7dR8mSH/versions/P7uGFii0/fabric-api-0.92.2%2B1.20.1.jar"
```

#### Verify Installation

Check that both mods are installed:

```bash
# For vanilla launcher
ls -lh ~/.minecraft/mods/

# For Snap launcher
ls -lh ~/snap/mc-installer/638/.minecraft/mods/

# You should see:
# fabric-api.jar          (~2.1M)
# rentapolt-0.1.0.jar     (~156K)
```

### Step 3: Launch & Connect

1. Open **Minecraft Launcher**
2. Select profile: **fabric-loader-0.17.3-1.20.1** (or similar Fabric profile)
3. Click **Play**
4. Once in the main menu, click **Multiplayer**
5. Click **Add Server**
6. Enter:
   - **Server Name**: Ion's World
   - **Server Address**: `localhost:25565` (same machine) or `192.168.1.111:25565` (from another computer)
7. Click **Done**
8. Double-click the server to join!

## 🎯 What You'll Find

### Biomes

- **City** - Neon metropolis with glowing blocks and multi-story buildings
- **Prairie** - Peaceful grasslands with prairie homes
- **Mutant Zone** - Toxic wasteland with mutant towers (boss spawns!)
- **Secret Bunker** - Underground networks with secret rooms

### Loot & Currency
- **Chaos Coins** - Currency found in all chests (3-200 per chest)
- **Artifacts** - 7 legendary items (Ancient Relic, Void Shard, Plasma Core, etc.)
- **Tiered Loot** - 5 tiers from Common to Legendary

### Boss Battles
- **Mega Mutant** (500 HP) - Spawns on Mutant Towers (20% chance)
  - Drops: Ancient Relic, Nether Star, 100-200 Chaos Coins
- **Ancient Phoenix** (400 HP) - Spawns on Floating Islands (15% chance)
  - Drops: Dragon Egg, Elytra, Phoenix Feathers
  - **Requires Fire Resistance potion to damage!**
- **Shadow King** (450 HP) - Spawns in Bunker Secret Rooms (50% chance)
  - Drops: Void Shard, Dimensional Key, legendary loot

### Custom Mobs
**Hostile:**
- Mutant Creeper, Mutant Zombie, Fire Golem, Plasma Beast, Shadow Serpent
- **AI Enhanced:** Mutants flank in formation and coordinate attacks!

**Passive:**
- Lions (pack hunting), Elephants (defensive), Phoenix (healing aura), Griffin (dive-bomb)
- **AI Enhanced:** Lions hunt in packs and protect elephants!

### Weapons & Armor
**Weapons:** Ion Sword, Lightning Hammer, Chaos Blade, Plasma Rifle, Shadow Dagger, Storm Bow

**Armor Sets (with abilities):**
- Phoenix Set - Fire immunity
- Speed Set - Speed boost
- Flight Set - Creative flight
- Invisibility Set - Invisibility when worn
- Heavy Set - Resistance

## 🔧 Server Commands

```bash
# View live server logs
cd docker && make logs-live

# Restart server
cd docker && make restart

# Stop server
cd docker && make stop

# Deploy updated mod
cd docker && make deploy

# Get into server console (type commands)
docker exec -it rentapolt_server rcon-cli
```

## 🎉 Have Fun!

The server is ready to explore. Remember to bring Fire Resistance potions for the Ancient Phoenix boss fight!
