CORES=$(nproc) && docker build -t jlibtorrent-android --build-arg CORES=${CORES} .
