# Mounts this repo's folder as a volume in the container's /frostwire-jlibtorrent
docker \
 run \
 --cpus=8 \
 --memory=8g \
 -v "$PWD/../../frostwire-jlibtorrent:/frostwire-jlibtorrent" \
 -it jlibtorrent-android
