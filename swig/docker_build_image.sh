if [[ "$(uname)" == "Linux" ]]; then
  export CORES=$(nproc)
elif [[ "$(uname)" == "Darwin" ]]; then
  export CORES=$(sysctl -n hw.ncpu)
else
  echo "docker_build_image.sh: Unsupported operating system: $(uname)"
  exit 1
fi

docker build -t jlibtorrent-android --build-arg CORES=${CORES} .
