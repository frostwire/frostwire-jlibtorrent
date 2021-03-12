# Mounts this repo's folder as a volume in the container's /frostwire-jlibtorrent create_folder_if_it_doesnt_exist
# Then executes the build scripts for android
docker run -v "$PWD/../../frostwire-jlibtorrent:/frostwire-jlibtorrent" -it jlibtorrent-android #"cd /frostwire-jlibtorrent/swig; ./build-android-arm.sh"
