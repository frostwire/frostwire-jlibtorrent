# One Step Build (It will be cached if nothing changed)
./docker_build_image.sh

# Mounts this repo's folder as a volume in the container's /frostwire-jlibtorrent create_folder_if_it_doesnt_exist
# Then executes the build scripts for android
docker \
 run \
 --cpus=4 \
 --memory=8gb \
 -v "$PWD/../../frostwire-jlibtorrent:/frostwire-jlibtorrent" \
 -it jlibtorrent-android \
 /bin/bash /build.sh  #for some reason it won't run the script
