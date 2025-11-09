# Rentapolt Development Roadmap

This roadmap tracks the development progress of Ion's World mod from initial implementation to full release.

## Legend

- ✅ **Complete** - Fully implemented and tested
- 🚧 **In Progress** - Currently being worked on
- 📋 **Planned** - Ready to start
- 💭 **Future** - Ideas for later

---

## Core Features (v0.1.0 - CURRENT)

### ✅ World Generation
- [x] Custom dimension override system
- [x] 4 custom biomes (City, Prairie, Mutant Zone, Secret Bunker)
- [x] Biome modification API integration
- [x] Procedural structure spawning

### ✅ Blocks & Items
- [x] Energy Block (glowing power source)
- [x] Teleporter Block (random teleportation)
- [x] Explosive Block (trap block)
- [x] City Glow Block (decorative lighting)
- [x] 6 custom weapons (Ion Sword, Lightning Hammer, Chaos Blade, etc.)
- [x] 5 armor sets with abilities (Light, Heavy, Shadow, Phoenix, Storm)

### ✅ Entities
- [x] 5 hostile mobs (Mutant Creeper, Zombie, Fire Golem, Plasma Beast, Shadow Serpent)
- [x] 4 passive mobs (Lion, Elephant, Phoenix, Griffin)
- [x] Custom mob abilities (Fire Aura, Lightning Strike, Toxic Cloud, etc.)
- [x] Passive mob auras (Strength, Resistance, Fire Immunity, Slow Falling)

### ✅ Structures
- [x] City buildings with loot chests
- [x] Prairie homes
- [x] Mutant towers
- [x] Underground bunkers

### ✅ Infrastructure
- [x] Docker deployment automation
- [x] Build and deployment scripts
- [x] VS Code tasks integration
- [x] Comprehensive documentation
- [x] Entropy fix for server startup

---

## Phase 1 - Polish & Assets (v0.2.0)

**Goal:** Make the mod visually and audibly complete

### 🚧 Textures & Models
- [ ] Custom entity textures
  - [ ] Lions (majestic mane, golden fur)
  - [ ] Elephants (gray with armor details)
  - [ ] Phoenixes (fire effects, wings)
  - [ ] Griffins (eagle/lion hybrid)
  - [ ] Mutant Creeper (glowing mutations)
  - [ ] Mutant Zombie (toxic appearance)
  - [ ] Fire Golem (lava cracks)
  - [ ] Plasma Beast (energy core)
  - [ ] Shadow Serpent (dark ethereal)
- [ ] Custom block textures
  - [ ] Energy Block (animated glow)
  - [ ] Teleporter Block (portal effect)
  - [ ] City Glow Block (neon aesthetic)
  - [ ] Explosive Block (warning markings)
- [ ] Custom item textures
  - [ ] 6 weapons (unique designs)
  - [ ] 5 armor sets (full sets with variants)
- [ ] 3D armor models
  - [ ] Light Set (sleek, minimal)
  - [ ] Heavy Set (bulky, protective)
  - [ ] Shadow Set (dark, mysterious)
  - [ ] Phoenix Set (wing details, fire motifs)
  - [ ] Storm Set (lightning accents)

### 🚧 Sound Design
- [ ] Weapon sounds
  - [ ] chaos_explosion.ogg (explosive impact)
  - [ ] ionic_lightning.ogg (electric zap)
  - [ ] plasma_beam.ogg (energy blast)
  - [ ] portal_woosh.ogg (teleportation)
- [ ] Ambient music
  - [ ] music.city.ogg (cyberpunk vibes)
  - [ ] music.prairie.ogg (peaceful melody)
  - [ ] music.mutant_zone.ogg (ominous tension)
  - [ ] music.secret_bunker.ogg (mysterious)
- [ ] Mob sounds
  - [ ] Lion roar, elephant trumpet
  - [ ] Phoenix cry, griffin screech
  - [ ] Mutant groans, plasma hum

---

## Phase 2 - Content Expansion (v0.3.0)

**Goal:** Add depth and gameplay variety

### ✅ Crafting & Recipes
- [x] Weapon crafting recipes
  - [x] Ion Sword (diamonds + energy blocks)
  - [x] Lightning Hammer (iron + lightning rods)
  - [x] Chaos Blade (netherite + obsidian)
  - [x] Plasma Rifle (redstone + amethyst)
  - [x] Shadow Dagger (echo shards + darkness)
  - [x] Storm Bow (string + copper)
- [x] Armor crafting recipes
  - [x] Light Set (leather + feathers)
  - [x] Heavy Set (iron blocks + anvils)
  - [x] Shadow Set (black wool + ender pearls)
  - [x] Phoenix Set (blaze rods + fire charges)
  - [x] Storm Set (copper + lightning rods)
- [x] Block crafting
  - [x] Energy Block recipe
  - [x] Teleporter Block recipe
  - [x] City Glow Block recipe

### ✅ Spawn Eggs & Creative Mode
- [x] Spawn eggs for all custom mobs
  - [x] Hostile mobs (5 eggs)
  - [x] Passive mobs (4 eggs)
- [x] Creative mode tab organization
- [x] Item groups for weapons, armor, blocks

### 📋 Structure Improvements
- [ ] Road generation between cities
  - [ ] Pathfinding algorithm
  - [ ] Dynamic road materials
- [ ] Multi-story city buildings
  - [ ] Interiors with furniture
  - [ ] Different building types (shops, homes, towers)
- [ ] Underground bunker networks
  - [ ] Connected tunnels
  - [ ] Secret rooms
- [ ] Ruined structures in Mutant Zone
  - [ ] Damaged buildings
  - [ ] Crater formations
- [ ] Floating islands (rare spawns)
  - [ ] Special loot
  - [ ] Phoenix nests

### 📋 Loot System Overhaul
- [ ] Tiered loot tables (Common → Legendary)
- [ ] Unique items per structure type
- [ ] Custom enchanted books
- [ ] Currency system (Chaos Coins)
- [ ] Rare artifact items

---

## Phase 3 - Gameplay Depth (v0.4.0)

**Goal:** Add progression and endgame content

### 💭 Quests & Achievements
- [ ] Achievement system
  - [ ] "Enter Rentapolt" (first spawn)
  - [ ] "Armor Collector" (obtain all sets)
  - [ ] "Mutant Slayer" (defeat 100 mutants)
  - [ ] "City Builder" (place 100 city blocks)
  - [ ] "Explorer" (visit all biomes)
- [ ] Advancement tree
  - [ ] Discovery advancements
  - [ ] Combat advancements
  - [ ] Collection advancements

### 💭 Boss Mobs
- [ ] Mega Mutant (Mutant Zone boss)
  - [ ] 500 HP
  - [ ] Multiple attack phases
  - [ ] Special drops
- [ ] Ancient Phoenix (Sky Islands boss)
  - [ ] Flying combat
  - [ ] Fire immunity required
  - [ ] Phoenix egg drop
- [ ] Shadow King (Secret Bunker boss)
  - [ ] Darkness mechanics
  - [ ] Summons shadow minions
  - [ ] Legendary shadow weapons

### 💭 Enhanced Entity AI
- [ ] Pack behavior
  - [ ] Lions hunt in groups
  - [ ] Elephants protect each other
- [ ] Flying patterns
  - [ ] Phoenixes circle high
  - [ ] Griffins dive-bomb
- [ ] Formation attacks
  - [ ] Mutants coordinate
  - [ ] Plasma beasts flank
- [ ] Taming system
  - [ ] Lions tameable with golden carrots
  - [ ] Elephants follow when fed hay
  - [ ] Phoenixes rebirth from ashes
- [ ] Entity interactions
  - [ ] Lions protect elephants
  - [ ] Phoenixes heal nearby players
  - [ ] Mutants avoid energy blocks

### 💭 New Biomes
- [ ] Sky Islands
  - [ ] Floating terrain
  - [ ] Phoenix spawns
  - [ ] Cloud blocks
  - [ ] Wind mechanics
- [ ] Toxic Wasteland
  - [ ] Worse than Mutant Zone
  - [ ] Poison effects
  - [ ] Rare resources
- [ ] Crystal Caverns
  - [ ] Underground biome
  - [ ] Glowing crystals
  - [ ] Energy ore deposits
- [ ] Neon District
  - [ ] Advanced city variant
  - [ ] Tech blocks
  - [ ] Holograms

---

## Phase 4 - Polish & Release (v1.0.0)

**Goal:** Production-ready release

### 💭 Configuration
- [ ] Config file (TOML/JSON)
  - [ ] Mob spawn rates
  - [ ] Structure density
  - [ ] Biome sizes
  - [ ] Difficulty multipliers
  - [ ] Enable/disable features
- [ ] In-game config GUI (Mod Menu integration)

### 💭 Performance Optimization
- [ ] Structure generation optimization
  - [ ] Async generation
  - [ ] Chunk loading improvements
- [ ] Entity performance
  - [ ] LOD for distant entities
  - [ ] AI optimization
- [ ] Texture optimization
  - [ ] Mipmaps
  - [ ] Atlasing

### 💭 Compatibility
- [ ] Test with Optifine
- [ ] Test with Sodium
- [ ] Test with Iris Shaders
- [ ] Mod integration (REI, JEI)
- [ ] Multiplayer testing
  - [ ] Sync issues
  - [ ] Armor abilities in MP
  - [ ] Structure generation conflicts

### 💭 Documentation
- [ ] Wiki setup
- [ ] Video tutorials
- [ ] Mod showcase
- [ ] API documentation for developers

---

## Quick Wins (Starting Now!)

High-impact, easy additions to start with:

### ✅ **Quick Win 1: Crafting Recipes** ⭐ COMPLETE
- [x] Add shaped recipes for all weapons
- [x] Add shaped recipes for all armor sets
- [x] Add recipes for custom blocks
- [x] Create recipe advancement triggers

### ✅ **Quick Win 2: Spawn Eggs** ⭐ COMPLETE
- [x] Generate spawn egg items
- [x] Add textures for spawn eggs
- [x] Register in creative tabs

### � **Quick Win 3: Sound Files** ⭐ STARTING NOW
- [ ] Find/create placeholder sounds
- [ ] Drop .ogg files in assets/rentapolt/sounds
- [ ] Fix warning messages

### ✅ **Quick Win 4: Loot Improvements** ⭐ COMPLETE
- [x] Add diamonds to city chests
- [x] Add netherite to bunker chests
- [x] Add custom items to mutant tower chests
- [x] Increase loot variety
- [x] Add enchanted books and rare items
- [x] Balanced loot pools with weights

### ✅ **Quick Win 5: Structure Variety** ⭐ COMPLETE
- [x] Randomize building heights (cities 4-20, towers 12-24, prairie 3-5)
- [x] Use different materials per structure (7 city palettes, 4 wood types, 5 tower materials)
- [x] Add decorative blocks (flags, fences, spikes, varied lighting)
- [x] Enhanced city buildings with randomized concrete/quartz/terracotta
- [x] Prairie homes with varied sizes and wood types
- [x] Mutant towers with imposing heights and decorative spikes

---

## Version History

- **v0.1.0** (Current) - Initial implementation with core features
- **v0.2.0** (Planned) - Polish & Assets phase
- **v0.3.0** (Planned) - Content Expansion phase
- **v0.4.0** (Planned) - Gameplay Depth phase
- **v1.0.0** (Planned) - Official release

---

**Last Updated:** November 9, 2025  
**Current Focus:** Phase 1 - Textures & Models  
**Recent Achievements:**
- ✅ Completed Quick Wins 1 & 2 (Crafting recipes and spawn eggs were already implemented!)
- ✅ Completed Quick Win 4 (Enhanced all loot tables with diamonds, netherite, enchanted books, totems)
- ✅ Completed Quick Win 5 (Structure variety - randomized materials, heights, decorations)
- ✅ Cities now use 7 different material palettes (quartz, concrete, terracotta variants)
- ✅ Buildings have varied heights (cities 4-20, towers 12-24) with weighted distribution
- ✅ Added decorative elements (flags, fences, spikes, 4 lighting types)
- 🚧 Next: Quick Win 3 (Sound files) or Phase 1 main focus (Custom textures and models)

