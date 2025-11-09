#!/bin/bash
# Test script to verify Docker setup

echo "Testing Rentapolt Docker Setup..."
echo ""

PASSED=0
FAILED=0

# Test 1: Check if required files exist
echo "Test 1: Checking required files..."
FILES=(
    "Dockerfile"
    "docker-compose.yml"
    "startup.sh"
    "server.properties"
    ".dockerignore"
)

for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "  ✓ $file exists"
        ((PASSED++))
    else
        echo "  ✗ $file missing"
        ((FAILED++))
    fi
done

# Test 2: Check if scripts are executable
echo ""
echo "Test 2: Checking script permissions..."
if [ -x "startup.sh" ]; then
    echo "  ✓ startup.sh is executable"
    ((PASSED++))
else
    echo "  ✗ startup.sh not executable"
    ((FAILED++))
fi

if [ -x "setup.sh" ]; then
    echo "  ✓ setup.sh is executable"
    ((PASSED++))
else
    echo "  ✗ setup.sh not executable"
    ((FAILED++))
fi

# Test 3: Validate docker-compose.yml
echo ""
echo "Test 3: Validating docker-compose.yml..."
if docker compose config > /dev/null 2>&1; then
    echo "  ✓ docker-compose.yml is valid"
    ((PASSED++))
else
    echo "  ✗ docker-compose.yml has errors"
    ((FAILED++))
fi

# Test 4: Check if mods directory exists
echo ""
echo "Test 4: Checking directory structure..."
if [ -d "mods" ]; then
    echo "  ✓ mods/ directory exists"
    ((PASSED++))
    
    # Check if mod exists
    if ls mods/*.jar 1> /dev/null 2>&1; then
        echo "  ✓ Mod JAR found in mods/"
        ((PASSED++))
    else
        echo "  ⚠ No mod JAR in mods/ (run setup.sh to copy)"
    fi
else
    echo "  ✗ mods/ directory missing"
    ((FAILED++))
fi

# Test 5: Check Docker daemon
echo ""
echo "Test 5: Checking Docker..."
if docker info > /dev/null 2>&1; then
    echo "  ✓ Docker is running"
    ((PASSED++))
else
    echo "  ✗ Docker is not running or not installed"
    ((FAILED++))
fi

# Results
echo ""
echo "================================================"
echo "Test Results:"
echo "  Passed: $PASSED"
echo "  Failed: $FAILED"
echo "================================================"

if [ $FAILED -eq 0 ]; then
    echo "✓ All tests passed!"
    echo ""
    echo "Ready to run:"
    echo "  ./setup.sh"
    echo ""
    echo "Or manually:"
    echo "  docker compose up -d"
    exit 0
else
    echo "✗ Some tests failed. Please fix the issues above."
    exit 1
fi
