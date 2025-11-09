# Rentapolt v1.0.0 Release Notes

**Release Date:** November 9, 2025  
**Minecraft Version:** 1.20.1  
**Fabric Loader:** 0.17.3+  
**Fabric API:** 0.92.2+

---

## 🎉 MAJOR RELEASE - NYC Megacity Complete!

This is the **first major release** of Rentapolt, Ion's custom Minecraft world mod. After extensive development and testing, the mod now features a fully explorable NYC-style megacity with complete building interiors, diverse building types, and extensive urban infrastructure.

---

## ✨ What's New in v1.0.0

### 🏙️ Complete Building Interiors

Every building in Rentapolt City is now **fully explorable** with:

- **Multi-floor layouts** - Proper floors every 3 blocks with seamless room connectivity
- **Glass entrance doors** - Welcoming entryways with decorative glass frames
- **Internal spiral staircases** - Connect all floors so you can explore top to bottom
- **Windows** - Placed strategically every 2 blocks on exterior walls for natural light
- **Elevator shafts** - In mega buildings over 40 blocks tall, with platforms every 12 blocks
- **Hallway lighting** - Sea Lantern fixtures illuminate each floor
- **Varied furniture** - Each building type has unique interior decorations

### 🏢 6 Different Building Types

Buildings now have **distinct purposes** with themed interiors:

#### 🛍️ Shops (Cyan Awning)
- **Height:** 5-10 blocks
- **Features:** Display counters, storage chests, mannequins (fence posts)
- **Purpose:** Browse and imagine shopping in a bustling NYC storefront

#### 🍴 Restaurants (Red Awning)
- **Height:** 5-10 blocks
- **Features:** Tables with chairs, kitchen areas with furnaces and crafting tables
- **Purpose:** Dine in style with proper seating arrangements

#### 🏦 Banks (Yellow Awning)
- **Height:** 10-25 blocks
- **Features:** Teller counters with security bars, iron vault door
- **Purpose:** Secure your valuables in a fortified financial institution

#### 🏨 Hotels (Purple Awning)
- **Height:** 10-25 blocks
- **Features:** Reception desk, lobby seating, hotel rooms with beds
- **Purpose:** Check in and rest in comfort across multiple floors

#### 🏢 Office Buildings (Blue Awning)
- **Height:** 25-45 blocks
- **Features:** Desks with "computers" (crafting tables), bookshelves, filing cabinets
- **Purpose:** Work your way up the corporate ladder, floor by floor

#### 🏠 Luxury Apartments (Green Awning)
- **Height:** 45-80 blocks
- **Features:** Beds, kitchens, couches, living spaces
- **Purpose:** Live in the clouds in NYC's finest high-rise residences

### 🚦 Urban Infrastructure

The streets are now **fully decorated** with:

- **Traffic lights** - Functional-looking signals at major intersections (red/yellow/green)
- **Crosswalks** - White striped pedestrian paths
- **Fire hydrants** - Classic red 2-block hydrants
- **Benches** - Oak stair seating on sidewalks
- **Trash cans** - Cauldrons for waste disposal
- **Mailboxes** - Blue 2-block mail collection boxes
- **Parking meters** - Iron bars with stone buttons
- **Street lamps** - 90% coverage, 4 blocks tall with bright lights
- **Urban trees** - 30% of buildings have decorative trees in planters

### 🚇 Subway System (NEW!)

Descend into NYC's underground with **subway entrances** (10% spawn rate):

- **Iconic green roof** - Classic NYC subway aesthetic
- **Glass entrance walls** - See-through walls with proper entryway
- **Underground staircase** - Descends 8 blocks to the platform
- **Platform area** - 7 blocks wide, 10 blocks long
- **Dual rail tracks** - Functional-looking subway lines
- **Platform lighting** - Sea Lanterns every 3 blocks
- **Stone brick construction** - Sturdy underground architecture

### 📏 Building Height Distribution

Experience **realistic city variation**:

- **10%** - Mega Skyscrapers (60-80 blocks) - *One World Trade Center vibes*
- **15%** - Super Skyscrapers (45-60 blocks) - *Empire State Building style*
- **15%** - Large Skyscrapers (35-45 blocks)
- **20%** - Tall Buildings (25-35 blocks)
- **20%** - Medium Buildings (15-25 blocks)
- **20%** - Low Buildings (5-15 blocks)

---

## 🔧 Technical Improvements

### Performance Optimizations
- ✅ **Eliminated server deadlock** - Changed from `ServerChunkEvents.CHUNK_LOAD` to `ServerTickEvents.END_WORLD_TICK`
- ✅ **Fast server startup** - Approximately 5 seconds from container start to ready
- ✅ **Smooth world generation** - 25 seconds for full spawn area generation
- ✅ **No lag during structure spawning** - Asynchronous generation prevents freezing

### Bug Fixes
- Fixed infinite hang during world generation
- Fixed structure spawning causing server timeout
- Fixed client-server mod version mismatches
- Corrected all Spanish comments to English

---

## 🎮 How to Use

### Server Installation
1. Download `rentapolt-1.0.0.jar` from the releases
2. Place in your Fabric server's `mods/` folder
3. Ensure Fabric API 0.92.2+ is installed
4. Start/restart the server
5. The world will generate with Rentapolt City biomes

### Client Installation
1. Install Fabric Loader 0.17.3+
2. Install Fabric API 0.92.2+
3. Download and place `rentapolt-1.0.0.jar` in `.minecraft/mods/`
4. Launch Minecraft 1.20.1
5. Connect to a Rentapolt server or create a world with the mod

### Docker Deployment (Advanced)
```bash
cd docker
make deploy    # Builds and deploys mod
make start     # Starts the server
make logs-live # Monitor server logs
```

---

## 📋 Full Feature List

### Core Systems
- ✅ Custom dimension override (modifies Overworld generation)
- ✅ 4 unique biomes (City, Prairie, Mutant Zone, Secret Bunker)
- ✅ Procedural structure generation with 6 building types
- ✅ Comprehensive loot system with 5 tiers
- ✅ 3 boss mobs with unique abilities
- ✅ Enhanced mob AI with 5 custom goal classes

### Blocks & Items
- ✅ 4 functional blocks (Energy, Teleporter, Explosive, City Glow)
- ✅ 6 custom weapons with special abilities
- ✅ 5 armor sets with passive effects
- ✅ Chaos Coins (currency for crafting/trading)
- ✅ 7 legendary artifacts

### Entities
- ✅ 5 hostile mobs (Mutant Creeper, Zombie, Fire Golem, Plasma Beast, Shadow Serpent)
- ✅ 4 passive mobs (Lion, Elephant, Phoenix, Griffin)
- ✅ 3 boss mobs (Mega Mutant, Ancient Phoenix, Shadow King)
- ✅ Custom mob abilities and auras

### Structures
- ✅ NYC-style city buildings (60-80 blocks tall)
- ✅ Prairie homes (cozy countryside)
- ✅ Mutant towers (ominous boss lairs)
- ✅ Underground bunkers (secret bases)
- ✅ Subway stations (urban transit)

---

## 🐛 Known Issues

- Armor textures are basic placeholders (enhancement planned for v1.1.0)
- Some furniture placement might overlap in small rooms
- Elevator shafts are decorative (not functional transportation)
- Subway tracks don't connect between stations (visual only)

---

## 🔮 What's Next

### Planned for v1.1.0
- Enhanced armor textures and 3D models
- Custom sound effects and ambient music
- Functional elevator system
- Connected subway tunnel network
- More building variety (museums, schools, hospitals)

### Future Ideas
- Vehicles (cars, helicopters)
- NPCs with quests
- Dynamic weather in City biome
- Building interior customization
- Multiplayer-specific features

---

## 🙏 Credits

**Development:** Built for Ion's Minecraft adventures  
**Engine:** Fabric Mod Loader + Minecraft 1.20.1  
**Inspiration:** New York City architecture and urban design  
**Special Thanks:** All players who tested and provided feedback!

---

## 📞 Support & Feedback

If you encounter any issues or have suggestions:
- Check `BUGFIXES.md` for known issues and solutions
- Review `CONNECT.md` for connection troubleshooting
- See `README.md` for comprehensive documentation

**Enjoy exploring Rentapolt City!** 🏙️✨
