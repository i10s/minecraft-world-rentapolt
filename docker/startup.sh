#!/bin/bash

echo "================================================"
echo "Starting Rentapolt Fabric Minecraft Server"
echo "================================================"

# Check if Rentapolt mod exists
if [ -f "/server/mods/rentapolt-0.1.0.jar" ] || [ -f "/server/mods/rentapolt-1.0.0.jar" ]; then
    echo "✓ Rentapolt mod found in /server/mods/"
else
    echo "⚠ WARNING: Rentapolt mod not found in /server/mods/"
    echo "Please ensure rentapolt-*.jar is copied to ./mods/ directory"
fi

# List all mods
echo ""
echo "Installed mods:"
ls -lh /server/mods/*.jar 2>/dev/null || echo "No mods found"
echo ""

# Copy server.properties if it doesn't exist in the container
if [ ! -f "/server/server.properties" ]; then
    echo "Creating default server.properties..."
    cat > /server/server.properties << EOF
# Rentapolt Minecraft Server Properties
server-port=25565
gamemode=survival
difficulty=normal
max-players=20
online-mode=true
white-list=false
spawn-protection=16
level-name=world
level-seed=
motd=Rentapolt: Ion's World - Powered by Fabric
enable-command-block=true
pvp=true
max-world-size=29999984
EOF
fi

echo "Starting Fabric server..."
echo "Java options: $JAVA_OPTS"
echo ""

# Start the server
exec java $JAVA_OPTS -jar fabric-server-launch.jar nogui
