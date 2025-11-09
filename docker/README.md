# Rentapolt Fabric Minecraft Server - Docker Setup

This Docker setup runs a Minecraft 1.20.1 Fabric server with the Rentapolt mod installed.

## Prerequisites

- Docker and Docker Compose installed
- The Rentapolt mod JAR file built from the main project
- At least 4GB of RAM available for the container

## Quick Start

### First-Time Setup

**1. Download Fabric Server** (one-time setup):

```bash
cd docker
./download-fabric.sh
```

**2. Deploy the Rentapolt Mod**:

```bash
# Automated method (recommended)
make deploy
```

This will:

- Build the Rentapolt mod from source
- Copy the latest JAR to the mods folder
- Restart the Docker container with the new mod

**3. Monitor Server Startup**:

```bash
make logs-live
# or: docker logs -f rentapolt_server
```

Wait for the message: **"Rentapolt world booted with endless chaos!"**

The server will be ready at `localhost:25565`

---

## How to Rebuild and Redeploy After Editing the Mod

After making changes to the Rentapolt mod source code, use this workflow:

### Option 1: Automated Deployment (Recommended)

```bash
cd docker
make deploy
```

This single command:

1. Builds the mod with Gradle
2. Finds the latest JAR in `build/libs/`
3. Copies it to the Docker mods folder
4. Restarts the container to load the new version

### Option 2: Manual Steps

**Step 1**: Build the mod

```bash
cd /home/i10s/Code/minecraft-world-rentapolt
./gradlew build
```

**Step 2**: Update the server

```bash
cd docker
./update-mod.sh
docker compose restart
```

**Step 3**: Verify deployment

```bash
docker logs rentapolt_server | grep -i rentapolt
```

You should see: `"Rentapolt world booted with endless chaos!"`

---

## Managing the Server

### Server Controls

#### Start the Server

```bash
make start
# or: docker compose up -d
```

#### Stop the Server

```bash
make stop
# or: docker compose down
```

#### Restart the Server

```bash
make restart
# or: docker compose restart
```

### Monitoring

#### View Logs

```bash
# Last 50 lines
make logs

# Follow in real-time
make logs-live
```

### Available Make Commands

Run `make help` to see all available commands:

- `make deploy` - Build mod, update server, and restart
- `make update-mod` - Rebuild mod and copy to mods folder
- `make start` - Start the container
- `make stop` - Stop the container
- `make restart` - Restart the container
- `make logs` - Show recent logs
- `make logs-live` - Follow logs in real-time
- `make clean` - Remove container and volumes (⚠️ deletes world data)

---

## Connecting to the Server

1. Launch Minecraft 1.20.1 with Fabric Loader installed
2. Go to Multiplayer > Add Server
3. Server Address: `localhost:25565`
4. Click Join Server

---

## Directory Structure

```text
docker/
├── Dockerfile              # Container definition
├── docker-compose.yml      # Docker Compose configuration
├── Makefile                # Convenience commands
├── update-mod.sh           # Mod deployment script
├── startup.sh              # Server startup script
├── server.properties       # Minecraft server configuration
├── .dockerignore           # Files to exclude from Docker build
├── README.md               # This file
├── mods/                   # Mod directory (volume mounted)
│   ├── rentapolt-0.1.0.jar
│   └── fabric-api.jar
├── world/                  # World data (volume mounted, auto-created)
├── config/                 # Server configs (volume mounted, auto-created)
└── logs/                   # Server logs (volume mounted, auto-created)
```

---

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

The startup script includes `-Djava.security.egd=file:/dev/urandom` to prevent this issue.

**If the server still hangs at "Generating keypair":**

This is caused by low system entropy. The keypair generation requires random data, and systems with low entropy (especially VMs or containers) can take 15-30 minutes to gather enough.

**Solutions:**

1. **Install haveged on your host system** (recommended):

   ```bash
   sudo apt-get install haveged
   sudo systemctl enable --now haveged
   ```

2. **Check system entropy:**

   ```bash
   cat /proc/sys/kernel/random/entropy_avail
   ```

   Should be above 1000. If it's below 256, the server will be slow to start.

3. **Just wait** - The server will eventually start (15-30 minutes on very low entropy systems)

4. **Generate system activity** - Move your mouse, type, run file operations to increase entropy faster

**Note:** This issue affects both Docker servers and local Minecraft client development (`./gradlew runClient`)

### Reset World Data

To start with a fresh world:

```bash
docker compose down
rm -rf world/
docker compose up -d
```

---

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

---

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

---

## Advanced: Custom Fabric Version

To use a different Fabric or Minecraft version, edit the Dockerfile:

```dockerfile
RUN wget https://meta.fabricmc.net/v2/versions/loader/MINECRAFT_VERSION/FABRIC_VERSION/INSTALLER_VERSION/server/jar -O fabric-server-launch.jar
```

Check available versions at: <https://fabricmc.net/use/server/>

---

## Support

For issues related to:

- **The Rentapolt mod**: Check the main project README and source code
- **Docker setup**: Review this README and Docker logs
- **Fabric server**: Visit <https://fabricmc.net/wiki/tutorial:installing_minecraft_fabric_server>
- **Minecraft server**: Visit <https://minecraft.fandom.com/wiki/Server.properties>

---

## License

This Docker setup is provided as-is for use with the Rentapolt mod.
The Rentapolt mod is licensed under CC0-1.0 (see main project LICENSE file).
