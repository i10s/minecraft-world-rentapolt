#!/bin/bash
# Quick setup script for Rentapolt Docker server

set -e

echo "================================================"
echo "Rentapolt Minecraft Server - Docker Setup"
echo "================================================"
echo ""

# Check if we're in the right directory
if [ ! -f "docker-compose.yml" ]; then
    echo "Error: Run this script from the docker/ directory"
    exit 1
fi

# Step 1: Build the mod
echo "Step 1: Building Rentapolt mod..."
cd ..
./gradlew clean build

if [ ! -f "build/libs/rentapolt-0.1.0.jar" ]; then
    echo "Error: Mod build failed. JAR not found at build/libs/rentapolt-0.1.0.jar"
    exit 1
fi

echo "✓ Mod built successfully"

# Step 2: Copy mod to Docker directory
echo ""
echo "Step 2: Copying mod to Docker mods directory..."
cd docker
mkdir -p mods
cp ../build/libs/rentapolt-0.1.0.jar mods/
echo "✓ Mod copied to mods/"

# Step 3: Create volume directories
echo ""
echo "Step 3: Creating volume directories..."
mkdir -p world config logs
echo "✓ Directories created"

# Step 4: Build Docker image
echo ""
echo "Step 4: Building Docker image..."
docker compose build

echo "✓ Docker image built"

# Step 5: Start the server
echo ""
echo "Step 5: Starting server..."
docker compose up -d

echo ""
echo "================================================"
echo "Server is starting!"
echo "================================================"
echo ""
echo "Monitor logs with:"
echo "  docker compose logs -f rentapolt_server"
echo ""
echo "Connect to server at: localhost:25565"
echo ""
echo "Stop server with:"
echo "  docker compose down"
echo ""
