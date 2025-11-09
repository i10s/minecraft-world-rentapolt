# Rentapolt Docker Server - Quick Reference

## Development Workflow

### Edit → Build → Deploy

After editing your mod in VS Code:

```bash
cd docker
make deploy
```

This single command:

1. Builds your mod with Gradle
2. Finds the latest JAR in `build/libs/`
3. Copies it to the server mods folder
4. Restarts the Docker container

Then monitor startup:

```bash
make logs-live
```

---

## Common Commands

```bash
# Start server
make start

# Stop server
make stop

# Restart server
make restart

# View logs (last 50 lines)
make logs

# Follow logs in real-time
make logs-live

# Full deployment (build + restart)
make deploy

# Update mod only (without restart)
make update-mod

# Help
make help
```

---

## First-Time Setup

### Option 1: Automated Setup (Recommended)

```bash
cd docker
./setup.sh
```

This script will:

1. Build the Rentapolt mod
2. Copy it to the mods directory
3. Build the Docker image
4. Start the server

### Option 2: Manual Setup

```bash
# 1. Download Fabric server
cd docker
./download-fabric.sh

# 2. Build and deploy mod
make deploy

# 3. Monitor startup
make logs-live
```

---

## Verification

After deploying, you should see in the logs:

```text
✓ Rentapolt mod found in /server/mods/
[INFO]: Rentapolt world booted with endless chaos!
```

---

## Troubleshooting

**Mod not updating?**

- Run `make logs` to check if the server restarted
- Verify the JAR timestamp: `ls -lh mods/`

**Server won't start?**

- Check logs: `make logs`
- Ensure Fabric API is present: `ls -lh mods/fabric-api.jar`

**Port conflict?**

- Edit `docker-compose.yml` to change the external port
- Example: `"25566:25565"` (use port 25566 instead of 25565)

**Server hangs at "Generating keypair"?**

- This is a low entropy issue (common on VMs/containers)
- Install haveged: `sudo apt-get install -y haveged && sudo systemctl enable --now haveged`
- Or just wait 15-30 minutes - it will eventually complete
- Check entropy: `cat /proc/sys/kernel/random/entropy_avail` (should be >1000)

---

## File Locations

- **Mod source**: `../src/main/java/`
- **Built JAR**: `../build/libs/rentapolt-0.1.0.jar`
- **Server mods**: `./mods/`
- **World data**: `./world/`
- **Server logs**: `./logs/`
- **Server config**: `./config/`

---

## Quick Tips

- **Auto-rebuild on save**: Use `./gradlew build --continuous` in another terminal
- **Backup world**: `docker compose down && tar -czf backup.tar.gz world/`
- **Fresh start**: `make clean` (⚠️ deletes all world data)
- **Server console**: `docker exec -it rentapolt_server bash`

---

For detailed documentation, see [README.md](README.md)

## Key Features

✅ **Automatic EULA acceptance** - No manual intervention needed  
✅ **Entropy fix included** - Uses `/dev/urandom` to prevent "Generating keypair" hang  
✅ **Data persistence** - World, config, and mods persist between restarts  
✅ **Easy management** - Simple docker-compose commands  
✅ **Production-ready** - Includes restart policy and proper resource limits  

---

## Connecting to the Server

1. Launch Minecraft 1.20.1 with Fabric Loader
2. Install Fabric API 0.92.2+ client-side
3. Multiplayer → Add Server
4. Server Address: `localhost:25565` (or your server IP)
5. Join and explore Rentapolt's custom biomes!

## Next Steps

- Read full documentation in `docker/README.md`
- Customize `server.properties` for your needs
- Add additional Fabric mods to the `mods/` directory
- Configure whitelist, ops, and permissions
- Set up regular backups
- Deploy to a cloud server for public access

## Support

For detailed documentation, see `docker/README.md`

For mod-specific issues, check the main project README at the repository root.
