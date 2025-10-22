# Check for arm64 architecture and exit if found
if [[ "$(uname -m)" == "arm64" ]]; then
  echo "docker_build_image.sh: We tried doing this on linux arm64 but the Android NDK tools are meant to run on x86_64. Exiting."
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
