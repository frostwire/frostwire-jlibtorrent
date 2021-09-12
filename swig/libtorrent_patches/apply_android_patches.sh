set -x
cp /frostwire-jlibtorrent/swig/libtorrent_patches/*.patch /src/libtorrent/
cd /src/libtorrent
ls -l | head
git checkout -- .
# Turns off TORRENT_USE_NETLINK and turns on TORRENT_USE_IFADDRS
patch -p1 < ifaddr.1.2.14.patch

# https://github.com/arvidn/libtorrent/pull/6465/files
patch -p1 < enum_net_iface_from_ifaddrs.patch

exit 0
