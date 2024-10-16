# Check for arm64 architecture and exit if found
if [[ "$(uname -m)" == "arm64" ]]; then
  echo "docker_build_image.sh: This script cannot run on arm64 systems due to missing dependencies in Ubuntu arm64 images or unsupported macOS arm64 environments. Please use an x86_64 environment. Exiting."
  exit 1
fi


if [[ "$(uname)" == "Linux" ]]; then
  export CORES=$(nproc)
elif [[ "$(uname)" == "Darwin" ]]; then
  export CORES=$(sysctl -n hw.ncpu)
else
  echo "docker_build_image.sh: Unsupported operating system: $(uname)"
  exit 1
fi

docker build -t jlibtorrent-android --build-arg CORES=${CORES} .
