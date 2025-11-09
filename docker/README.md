# Rentapolt Fabric Minecraft Server - Docker Setup

This Docker setup runs a Minecraft 1.20.1 Fabric server with the Rentapolt mod installed.

## Prerequisites

- Docker and Docker Compose installed
- The Rentapolt mod JAR file built from the main project
- At least 4GB of RAM available for the container

## Quick Start

### 1. Build the Rentapolt Mod

From the main project directory:

```bash
cd /home/i10s/Code/minecraft-world-rentapolt
./gradlew clean build
```

The mod JAR will be created at: `build/libs/rentapolt-0.1.0.jar`

### 2. Copy the Mod to Docker Directory

```bash
cd docker
mkdir -p mods
cp ../build/libs/rentapolt-0.1.0.jar mods/
```

### 3. Build and Start the Docker Container

```bash
docker compose up -d
```

This will:
- Download the Fabric server for Minecraft 1.20.1
- Install the Rentapolt mod
- Accept the EULA automatically
- Start the server on port 25565

### 4. Monitor Server Logs

```bash
docker compose logs -f rentapolt_server
```

Wait for the message: **"Rentapolt world loaded successfully – powered by Fabric."**

### 5. Connect to the Server

1. Launch Minecraft 1.20.1 with Fabric Loader installed
2. Go to Multiplayer > Add Server
3. Server Address: `localhost:25565`
4. Click Join Server

## Directory Structure

```
docker/
├── Dockerfile              # Container definition
├── docker-compose.yml      # Docker Compose configuration
├── startup.sh              # Server startup script
├── server.properties       # Minecraft server configuration
├── .dockerignore          # Files to exclude from Docker build
├── README.md              # This file
├── mods/                  # Mod directory (volume mounted)
│   └── rentapolt-0.1.0.jar
├── world/                 # World data (volume mounted, auto-created)
├── config/                # Server configs (volume mounted, auto-created)
└── logs/                  # Server logs (volume mounted, auto-created)
```

## Common Commands

### Start the Server
```bash
docker compose up -d
```

### Stop the Server
```bash
docker compose down
```

### Restart the Server
```bash
docker compose restart
```

### View Live Logs
```bash
docker compose logs -f rentapolt_server
```

### Execute Commands in the Server Console
```bash
docker exec -i rentapolt_server bash -c 'echo "say Hello from Docker!" > /dev/stdin'
```

Or attach to the console:
```bash
docker attach rentapolt_server
```
(Use `Ctrl+P` then `Ctrl+Q` to detach without stopping the server)

### Access Server Shell
```bash
docker exec -it rentapolt_server bash
```

### Rebuild After Code Changes
```bash
# Rebuild the mod
cd /home/i10s/Code/minecraft-world-rentapolt
./gradlew clean build

# Copy new mod version
cd docker
cp ../build/libs/rentapolt-0.1.0.jar mods/

# Restart container
docker compose restart
```

## Configuration

### Server Properties

Edit `server.properties` before starting the container to customize:
- `max-players` - Maximum number of players (default: 20)
- `difficulty` - Game difficulty (peaceful, easy, normal, hard)
- `gamemode` - Default game mode (survival, creative, adventure, spectator)
- `level-seed` - World seed
- `motd` - Server message of the day
- `view-distance` - Render distance (default: 10)

### Memory Allocation

Edit `docker-compose.yml` to change memory:

```yaml
environment:
  - JAVA_OPTS=-Xmx4G -Xms2G -Djava.security.egd=file:/dev/urandom
```

- `-Xmx4G` - Maximum heap size
- `-Xms2G` - Initial heap size

### Port Mapping

To use a different port, edit `docker-compose.yml`:

```yaml
ports:
  - "25566:25565"  # External:Internal
```

Then update `server.properties`:
```properties
server-port=25565
```

## Data Persistence

The following directories are mounted as Docker volumes and persist between container restarts:

- `./mods` - Installed mods
- `./world` - World save data
- `./config` - Server and mod configurations
- `./logs` - Server logs
- `./server.properties` - Server properties file

## Troubleshooting

### Server Won't Start

Check logs:
```bash
docker compose logs rentapolt_server
```

### Mod Not Loading

Verify the mod is in the mods directory:
```bash
ls -lh mods/
```

Check container mods:
```bash
docker exec rentapolt_server ls -lh /server/mods/
```

### Port Already in Use

If port 25565 is already in use, change the external port in `docker-compose.yml`:
```yaml
ports:
  - "25566:25565"
```

### Container Keeps Restarting

Check if you have enough RAM:
```bash
docker stats rentapolt_server
```

Reduce memory allocation in `docker-compose.yml` if needed.

### "Generating keypair" Hang

The startup script includes `-Djava.security.egd=file:/dev/urandom` to prevent this issue. If it still occurs, install `haveged` on your host:

```bash
sudo apt-get install haveged
sudo systemctl enable haveged
sudo systemctl start haveged
```

### Reset World Data

To start with a fresh world:
```bash
docker compose down
rm -rf world/
docker compose up -d
```

## Server Administration

### Op Yourself

```bash
docker exec rentapolt_server bash -c 'echo "op YourMinecraftUsername" >> /server/ops.txt'
docker compose restart
```

### Whitelist Players

Enable whitelist in `server.properties`:
```properties
white-list=true
```

Add players:
```bash
docker exec rentapolt_server bash -c 'echo "whitelist add PlayerName" >> /server/whitelist.json'
```

### Backup World Data

```bash
# Stop server
docker compose down

# Create backup
tar -czf backup-$(date +%Y%m%d-%H%M%S).tar.gz world/ config/

# Start server
docker compose up -d
```

## Production Deployment

For running on a remote server:

1. Update `server.properties`:
   ```properties
   server-ip=0.0.0.0
   online-mode=true
   ```

2. Configure firewall to allow port 25565

3. Use `docker-compose.yml` with restart policy:
   ```yaml
   restart: unless-stopped
   ```

4. Consider using a reverse proxy (nginx) for additional security

## Advanced: Custom Fabric Version

To use a different Fabric or Minecraft version, edit the Dockerfile:

```dockerfile
RUN wget https://meta.fabricmc.net/v2/versions/loader/MINECRAFT_VERSION/FABRIC_VERSION/INSTALLER_VERSION/server/jar -O fabric-server-launch.jar
```

Check available versions at: https://fabricmc.net/use/server/

## Support

For issues related to:
- **The Rentapolt mod**: Check the main project README and source code
- **Docker setup**: Review this README and Docker logs
- **Fabric server**: Visit https://fabricmc.net/wiki/tutorial:installing_minecraft_fabric_server
- **Minecraft server**: Visit https://minecraft.fandom.com/wiki/Server.properties

## License

This Docker setup is provided as-is for use with the Rentapolt mod.
The Rentapolt mod is licensed under CC0-1.0 (see main project LICENSE file).
