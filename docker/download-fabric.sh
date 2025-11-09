#!/bin/bash
# Download Fabric server installer locally using alternative methods

set -e

echo "================================================"
echo "Downloading Fabric Server for Minecraft 1.20.1"
echo "================================================"
echo ""

cd "$(dirname "$0")"

# Try multiple download sources
echo "Step 1: Downloading Fabric installer..."

# Use latest stable installer version
INSTALLER_VERSION="0.11.2"
echo "Using Fabric Installer version $INSTALLER_VERSION"

if ! wget -O fabric-installer.jar "https://maven.fabricmc.net/net/fabricmc/fabric-installer/$INSTALLER_VERSION/fabric-installer-$INSTALLER_VERSION.jar"; then
    echo "Failed to download Fabric installer"
    exit 1
fi

echo "✓ Fabric installer downloaded"
echo ""

# Install Fabric server
echo "Step 2: Installing Fabric server..."
java -jar fabric-installer.jar server -mcversion 1.20.1 -loader 0.17.3 -downloadMinecraft

echo "✓ Fabric server installed"
echo ""

# Clean up installer
rm fabric-installer.jar

echo "Step 3: Verifying files..."
if [ -f "fabric-server-launch.jar" ]; then
    echo "✓ fabric-server-launch.jar found"
else
    echo "✗ fabric-server-launch.jar not found!"
    exit 1
fi

if [ -f "server.jar" ]; then
    echo "✓ server.jar found"
fi

if [ -d "libraries" ]; then
    echo "✓ libraries directory found"
fi

echo ""
echo "================================================"
echo "Fabric server ready!"
echo "================================================"
echo ""
echo "Files created:"
echo "  - fabric-server-launch.jar (Fabric launcher)"
echo "  - server.jar (Minecraft server)"
echo "  - libraries/ (dependencies)"
echo ""
echo "Next step: docker compose build"
echo ""
