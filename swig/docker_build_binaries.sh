#!/bin/bash
# One Step Build (It will be cached if nothing changed)
./docker_build_image.sh || { echo "docker_build_binaries.sh: docker_build_image.sh failed. Exiting."; exit 1; }

# Function to get 80% of free memory in GB
get_available_memory() {
    local free_mem
    if [[ "$(uname)" == "Linux" ]]; then
        # Linux
        free_mem_kb=$(awk '/MemAvailable/ {print $2}' /proc/meminfo)
        free_mem=$(echo "$free_mem_kb / 1024 / 1024" | bc -l)  # Convert to GB
    elif [[ "$(uname)" == "Darwin" ]]; then
        # macOS
        # Get page size
        page_size=$(sysctl -n hw.pagesize)
        # Get number of free pages
        free_pages=$(vm_stat | awk '/Pages free/ {print $3}' | sed 's/\.//')
        # Get number of inactive pages (considered as available)
        inactive_pages=$(vm_stat | awk '/Pages inactive/ {print $3}' | sed 's/\.//')
        # Calculate free memory in bytes
        free_mem_bytes=$(( (free_pages + inactive_pages) * page_size ))
        # Convert to GB
        free_mem=$(echo "$free_mem_bytes / 1024 / 1024 / 1024" | bc -l)
    else
        echo "docker_build_binaries.sh: Unsupported OS"
        exit 1
    fi

    # Calculate 80% of free memory
    mem_limit=$(echo "$free_mem * 0.8" | bc -l)

    # Round down to the nearest whole number to avoid over-allocation
    mem_limit_int=$(echo "$mem_limit" | awk '{printf("%d\n",$1)}')

    # Ensure at least 1 GB is allocated
    if [[ "$mem_limit_int" -lt 1 ]]; then
        mem_limit_int=1
    fi

    echo "${mem_limit_int}gb"
}

# Function to get the number of CPU cores
get_num_cpus() {
    local num_cpus
    if [[ "$(uname)" == "Linux" ]]; then
        num_cpus=$(nproc)
    elif [[ "$(uname)" == "Darwin" ]]; then
        num_cpus=$(sysctl -n hw.ncpu)
    else
        echo "docker_build_binaries.sh::get_num_cpus(): Unsupported OS"
        exit 1
    fi
    echo "$num_cpus"
}

TOTAL_MEMORY=$(get_available_memory)
NUM_CPUS=$(get_num_cpus)

echo "docker_build_binaries.sh: Total memory assigned to Docker: $TOTAL_MEMORY"
echo "docker_build_binaries.sh: Number of CPUs assigned to Docker: $NUM_CPUS"

# Mounts this repo's folder as a volume in the container's /frostwire-jlibtorrent create_folder_if_it_doesnt_exist
# Then executes the build scripts for android
docker run \
    --cpus=$NUM_CPUS \
    --memory=$TOTAL_MEMORY \
    -v "$PWD/../../frostwire-jlibtorrent:/frostwire-jlibtorrent" \
    -it jlibtorrent-android \
    /bin/bash -c "/build_desktop.sh && /build_android_all.sh"
