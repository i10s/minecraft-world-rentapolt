# Bug Fixes

## November 9, 2025 - Fixed Server Startup Deadlock

### Problem

The server would hang indefinitely during world generation at "Preparing level 'world'" phase. This was caused by structure generation code trying to modify chunks during the initial chunk loading phase, creating a deadlock.

### Root Cause

`RentapoltStructureSpawner.java` was using `ServerChunkEvents.CHUNK_LOAD` to generate structures. This event fires during the initial world generation, and attempting to modify chunks while they're being loaded caused a circular dependency:

- Server tries to load spawn chunks
- CHUNK_LOAD event fires for each chunk
- Event handler tries to generate structures (which loads more chunks)
- Those chunks fire CHUNK_LOAD events
- **Deadlock**

### Solution

Changed structure generation to use `ServerTickEvents.END_WORLD_TICK` instead:

1. **Initialization**: Use `ServerWorldEvents.LOAD` to mark when the Overworld is ready
2. **Deferred Generation**: Generate structures asynchronously during server ticks
3. **Player-Centric**: Only generate structures in chunks near players (within 2 chunk radius)
4. **Tracked Generation**: Use `ConcurrentHashMap` to track which chunks have been processed

### Benefits

- Server starts in ~5 seconds instead of hanging forever
- Structures generate dynamically as players explore
- No performance impact on world initialization
- Compatible with both new and existing worlds

### Files Changed

- `src/main/java/com/rentapolt/world/RentapoltStructureSpawner.java`
  - Replaced `ServerChunkEvents.CHUNK_LOAD` with `ServerTickEvents.END_WORLD_TICK`
  - Added `ServerWorldEvents.LOAD` for initialization tracking
  - Added player-centric chunk processing
  - Extracted structure generation logic to `generateStructuresInChunk()` method

### Testing

✅ Server starts successfully with mod enabled  
✅ World generates without hanging  
✅ Structures spawn correctly in biomes  
✅ Client can connect without timeout errors  
✅ No performance degradation

### Deployment

```bash
# Build updated mod
./gradlew build

# Deploy to server
cd docker && make update-mod
docker compose restart

# Update client (for Snap launcher)
cp build/libs/rentapolt-0.1.0.jar ~/snap/mc-installer/638/.minecraft/mods/

# Update client (for vanilla launcher)
cp build/libs/rentapolt-0.1.0.jar ~/.minecraft/mods/
```
