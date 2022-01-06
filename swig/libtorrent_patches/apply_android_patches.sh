set -x
cp /frostwire-jlibtorrent/swig/libtorrent_patches/*.patch /src/libtorrent/
cd /src/libtorrent
ls -l | head
git checkout -- .
# Turns off TORRENT_USE_NETLINK and turns on TORRENT_USE_IFADDRS
patch -p1 < ifaddr.1.2.14.patch

exit 0
