# Rentapolt: Ion's World

Rentapolt is a Fabric mod for Minecraft 1.20.1 that replaces the Overworld with Ion's chaotic playground. Custom biomes, mobs, armor sets, weapons, and structures turn survival into an overpowered adventure built for Ion and his dad.

## Highlights
- **New Overworld** – the default dimension is replaced with Rentapolt biomes like the neon City, peaceful Prairie, toxic Mutant Zone, and underground Secret Bunker fields.
- **Custom structures** – procedurally generated roads, towers, houses, and bunkers filled with bespoke loot tables.
- **Armory upgrades** – six unique weapons and five full armor sets with gameplay abilities such as flight, invisibility, burn immunity, lightning strikes, and teleportation.
- **Creatures and allies** – hostile mutants (creepers, zombies, fire golems, plasma beasts, serpents) plus peaceful lions, elephants, phoenixes, and griffins with aura buffs.
- **Blocks & tech** – Energy blocks, random Teleporters, Explosive traps, and glowing city décor integrated with crafting recipes and loot.
- **Audio & ambience** – custom sound events and music cues for portal ignitions, lightning slams, plasma bursts, and mutant biomes.

## Building & running
1. Install a Java 17 JDK.
2. From the repository root run:
   ```bash
   ./gradlew genSources
   ./gradlew runClient
   ```
3. Launch a new world; the Overworld will use the Rentapolt generator automatically.

## Project layout
- `src/main/java/com/rentapolt` – mod entry point, blocks, items, entities, armor logic, and worldgen builders.
- `src/main/resources/assets/rentapolt` – language keys, models, textures, placeholder art, and sound definitions.
- `src/main/resources/data/rentapolt` – recipes, loot tables, biomes, structures, and tag data.
- `src/main/resources/data/minecraft/dimension/overworld.json` – overrides vanilla Overworld to point at Rentapolt biomes/features.

Have fun storming Ion's world! Contributions and tweaks are welcome—just keep the chaos cranked to 11.
