#!/bin/bash
set -e

# update-mod.sh - Automatically rebuild and deploy the Rentapolt mod to Docker

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

MOD_PROJECT_DIR="../"
MOD_BUILD_DIR="${MOD_PROJECT_DIR}/build/libs"
SERVER_MODS_DIR="./mods"

echo -e "${BLUE}=== Rentapolt Mod Deployment ===${NC}"

# Step 1: Build the mod if needed
if [ ! -d "$MOD_BUILD_DIR" ] || [ -z "$(ls -A $MOD_BUILD_DIR/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}No built mod found. Running Gradle build...${NC}"
    cd "$MOD_PROJECT_DIR"
    ./gradlew clean build
    cd - > /dev/null
else
    echo -e "${GREEN}✓ Mod build directory exists${NC}"
fi

# Step 2: Find the newest JAR (exclude -sources.jar and -dev.jar)
echo "Finding latest mod JAR..."
LATEST_JAR=$(find "$MOD_BUILD_DIR" -name "rentapolt-*.jar" ! -name "*-sources.jar" ! -name "*-dev.jar" -type f -printf '%T@ %p\n' | sort -rn | head -1 | cut -d' ' -f2-)

if [ -z "$LATEST_JAR" ]; then
    echo -e "${YELLOW}Error: No rentapolt JAR found in $MOD_BUILD_DIR${NC}"
    exit 1
fi

JAR_NAME=$(basename "$LATEST_JAR")
echo -e "${GREEN}Found: $JAR_NAME${NC}"

# Step 3: Create mods directory if it doesn't exist
mkdir -p "$SERVER_MODS_DIR"

# Step 4: Remove old rentapolt versions
echo "Removing old mod versions..."
rm -f "$SERVER_MODS_DIR"/rentapolt-*.jar 2>/dev/null || true

# Step 5: Copy the new JAR
echo "Copying $JAR_NAME to server mods folder..."
cp "$LATEST_JAR" "$SERVER_MODS_DIR/"

echo -e "${GREEN}✓ Deployment complete!${NC}"
echo -e "${BLUE}Next step: Run 'docker compose restart' to reload the server${NC}"
echo ""
echo "To verify, run: docker logs rentapolt_server | grep -i rentapolt"
