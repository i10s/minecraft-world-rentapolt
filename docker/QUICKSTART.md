# Rentapolt Docker Server - Quick Reference

## Files Created

```
docker/
├── Dockerfile              # Container image definition
├── docker-compose.yml      # Docker Compose orchestration
├── startup.sh              # Server startup script
├── setup.sh                # Automated setup script
├── server.properties       # Minecraft server configuration
├── .dockerignore          # Docker build exclusions
├── .gitignore             # Git exclusions
├── README.md              # Full documentation
└── mods/
    └── .gitkeep           # Directory placeholder
```

## Quick Start

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
# 1. Build the mod
cd /home/i10s/Code/minecraft-world-rentapolt
./gradlew clean build

# 2. Copy mod to docker directory
cd docker
mkdir -p mods
cp ../build/libs/rentapolt-0.1.0.jar mods/

# 3. Start the server
docker compose up -d

# 4. Monitor logs
docker compose logs -f rentapolt_server
```

## Key Features

✅ **Automatic EULA acceptance** - No manual intervention needed  
✅ **Entropy fix included** - Uses `/dev/urandom` to prevent "Generating keypair" hang  
✅ **Data persistence** - World, config, and mods persist between restarts  
✅ **Easy management** - Simple docker-compose commands  
✅ **Production-ready** - Includes restart policy and proper resource limits  

## Important Commands

```bash
# Start server
docker compose up -d

# Stop server
docker compose down

# View logs
docker compose logs -f rentapolt_server

# Restart server
docker compose restart

# Update mod
cp ../build/libs/rentapolt-0.1.0.jar mods/
docker compose restart
```

## Configuration

### Memory Settings

Edit `docker-compose.yml`:
```yaml
environment:
  - JAVA_OPTS=-Xmx4G -Xms2G -Djava.security.egd=file:/dev/urandom
```

### Server Settings

Edit `server.properties` before first run:
- `max-players=20` - Maximum players
- `difficulty=normal` - Game difficulty
- `gamemode=survival` - Default game mode
- `motd=Rentapolt: Ion's World - Powered by Fabric` - Server description

### Port Mapping

Default: `25565:25565` (host:container)

To change, edit `docker-compose.yml`:
```yaml
ports:
  - "25566:25565"  # Use port 25566 on host
```

## Troubleshooting

### Check if server is running
```bash
docker ps | grep rentapolt
```

### View real-time logs
```bash
docker compose logs -f rentapolt_server
```

### Check mod installation
```bash
docker exec rentapolt_server ls -lh /server/mods/
```

### Connect to server console
```bash
docker attach rentapolt_server
# Press Ctrl+P then Ctrl+Q to detach
```

### Server won't start
1. Check logs: `docker compose logs rentapolt_server`
2. Verify mod exists: `ls -lh mods/`
3. Check memory: `docker stats rentapolt_server`
4. Verify port not in use: `netstat -ln | grep 25565`

## Data Locations

All data is stored in the `docker/` directory:

- `mods/` - Server mods (including Rentapolt)
- `world/` - World save data (auto-created)
- `config/` - Server and mod configs (auto-created)
- `logs/` - Server logs (auto-created)
- `server.properties` - Server settings

## Backup & Restore

### Backup
```bash
docker compose down
tar -czf backup-$(date +%Y%m%d).tar.gz world/ config/ mods/
docker compose up -d
```

### Restore
```bash
docker compose down
tar -xzf backup-YYYYMMDD.tar.gz
docker compose up -d
```

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
