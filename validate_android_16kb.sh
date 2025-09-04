#!/bin/bash

# Script to validate Android 16 KB page size configuration
# This script checks if the necessary linker flags are present in all Android config files

echo "=== Android 16 KB Page Size Configuration Validator ==="
echo ""

CONFIG_DIR="swig/config"
REQUIRED_FLAG="--max-page-size=16384"
ANDROID_CONFIGS=("android-arm-config.jam" "android-arm64-config.jam" "android-x86-config.jam" "android-x86_64-config.jam")

all_configs_valid=true

for config in "${ANDROID_CONFIGS[@]}"; do
    config_path="${CONFIG_DIR}/${config}"
    
    if [ -f "$config_path" ]; then
        echo "Checking $config..."
        if grep -q -- "$REQUIRED_FLAG" "$config_path"; then
            echo "  ✅ Contains $REQUIRED_FLAG"
        else
            echo "  ❌ Missing $REQUIRED_FLAG"
            all_configs_valid=false
        fi
    else
        echo "  ❌ Configuration file not found: $config_path"
        all_configs_valid=false
    fi
done

echo ""
if $all_configs_valid; then
    echo "🎉 All Android configurations are properly set up for 16 KB page size support!"
    echo ""
    echo "Next steps:"
    echo "  1. Build the native libraries: cd swig && ./docker_build_binaries.sh"
    echo "  2. Test on a 16 KB page size device: adb shell getconf PAGE_SIZE"
    exit 0
else
    echo "❌ Some configurations are missing the required page size flag."
    echo "Please ensure all Android config files contain: <linkflags>-Wl,$REQUIRED_FLAG"
    exit 1
fi