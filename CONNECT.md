# Quick Connection Guide

## 🎮 Server Status

✅ **Server is RUNNING!**
- Address: `192.168.1.111:25565` (or `localhost:25565` from this machine)
- Version: Minecraft 1.20.1 with Fabric
- Port: 25565

## 📦 Connect from Minecraft Client

### Step 1: Install Fabric
1. Download **Fabric Loader** for Minecraft 1.20.1 from https://fabricmc.net/use/installer/
2. Run the installer and select "Install Client" for version 1.20.1
3. Launch Minecraft and select the "Fabric 1.20.1" profile

### Step 2: Install Required Mods (Client-Side)
You need to install the Rentapolt mod on your client to see custom items/blocks/entities:

```bash
# From this project directory:
./gradlew build

# Copy the mod JAR to your Minecraft mods folder:
# The built JAR is in: build/libs/rentapolt-1.0.0.jar

# Linux/Mac:
cp build/libs/rentapolt-1.0.0.jar ~/.minecraft/mods/

# Windows:
# Copy to: %APPDATA%\.minecraft\mods\
```

**Also install Fabric API:**
- Download from: https://modrinth.com/mod/fabric-api
- Place in the same `mods` folder

### Step 3: Connect to Server
1. Launch Minecraft 1.20.1 with Fabric
2. Click **Multiplayer**
3. Click **Add Server**
4. Enter:
   - **Server Name**: Ion's World
   - **Server Address**: `192.168.1.111:25565`
   - (Or use `localhost:25565` if connecting from this same computer)
5. Click **Done**
6. Double-click the server to join!

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
