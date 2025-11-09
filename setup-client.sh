#!/bin/bash
# Rentapolt Client Setup Script
# This script helps you get the client ready to connect to the server

set -e

echo "🎮 Rentapolt Client Setup"
echo "=========================="
echo ""

# Check if Minecraft directory exists
MINECRAFT_DIR="$HOME/.minecraft"
MODS_DIR="$MINECRAFT_DIR/mods"

if [ ! -d "$MINECRAFT_DIR" ]; then
    echo "❌ Minecraft directory not found at $MINECRAFT_DIR"
    echo "   Please run Minecraft launcher at least once to create the directory."
    exit 1
fi

echo "✅ Minecraft directory found"

# Create mods directory if it doesn't exist
mkdir -p "$MODS_DIR"
echo "✅ Mods directory ready: $MODS_DIR"
echo ""

# Copy Rentapolt mod
MOD_SOURCE="$(dirname "$0")/build/libs/rentapolt-0.1.0.jar"
MOD_DEST="$MODS_DIR/rentapolt-0.1.0.jar"

if [ ! -f "$MOD_SOURCE" ]; then
    echo "⚠️  Mod JAR not found. Building now..."
    ./gradlew build
fi

cp "$MOD_SOURCE" "$MOD_DEST"
echo "✅ Rentapolt mod installed: $MOD_DEST"
echo ""

# Check for Fabric API
FABRIC_API=$(find "$MODS_DIR" -name "fabric-api-*.jar" 2>/dev/null | head -1)

if [ -z "$FABRIC_API" ]; then
    echo "⚠️  Fabric API not found in mods folder!"
    echo ""
    echo "📥 You need to download Fabric API manually:"
    echo "   1. Go to: https://modrinth.com/mod/fabric-api/versions"
    echo "   2. Download the version for Minecraft 1.20.1"
    echo "   3. Place it in: $MODS_DIR"
    echo ""
else
    echo "✅ Fabric API found: $(basename "$FABRIC_API")"
    echo ""
fi

# Server information
SERVER_IP=$(hostname -I | awk '{print $1}')

echo "🎯 Next Steps:"
echo "=============="
echo ""
echo "1. Install Fabric Loader for Minecraft 1.20.1:"
echo "   - Download from: https://fabricmc.net/use/installer/"
echo "   - Run installer, select 'Install Client'"
echo "   - Choose Minecraft version: 1.20.1"
echo "   - Loader version: 0.17.3 (or latest)"
echo ""

if [ -z "$FABRIC_API" ]; then
    echo "2. Download Fabric API (REQUIRED):"
    echo "   - Go to: https://modrinth.com/mod/fabric-api/versions"
    echo "   - Download for Minecraft 1.20.1"
    echo "   - Place in: $MODS_DIR"
    echo ""
    echo "3. Launch Minecraft:"
else
    echo "2. Launch Minecraft:"
fi
echo "   - Open Minecraft Launcher"
echo "   - Select profile: 'Fabric Loader 0.17.3 - 1.20.1'"
echo "   - Click 'Play'"
echo ""
echo "4. Connect to Server:"
echo "   - Click 'Multiplayer'"
echo "   - Click 'Add Server'"
echo "   - Server Address: localhost:25565"
echo "   - Or from another computer: $SERVER_IP:25565"
echo ""

echo "📋 Installed Mods:"
echo "=================="
ls -lh "$MODS_DIR"/*.jar 2>/dev/null || echo "No mods found"
echo ""

echo "✅ Setup complete!"
echo ""
echo "Server is running at: $SERVER_IP:25565"
echo "Check server logs: cd docker && make logs-live"
