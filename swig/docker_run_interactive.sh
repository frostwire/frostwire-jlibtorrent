# Mounts this repo's folder as a volume in the container's /frostwire-jlibtorrent
docker \
 run \
 --cpus=16 \
 --memory=100g \
 -v "$PWD/../../frostwire-jlibtorrent:/frostwire-jlibtorrent" \
 -it jlibtorrent-android
