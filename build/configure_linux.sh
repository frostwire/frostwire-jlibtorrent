#!/bin/bash
source ./build/environment_linux.sh
sanityCheck

# Some setup
sudo apt-get install libbz2-dev libssl1.0.0 libcrypto++9 subversion

# Download BOOST to BOOST_ROOT folder.
mkdir -p $BOOST_ROOT
pushd $BOOST_ROOT
wget -O boost_$BOOST_UNDERSCORED_VERSION.tar.bz2 http://sourceforge.net/projects/boost/files/boost/$BOOST_DOTTED_VERSION/boost_$BOOST_UNDERSCORED_VERSION.tar.bz2/download
tar -xvf boost_$BOOST_UNDERSCORED_VERSION.tar.bz2
popd

# Download libtorrent to LIBTORRENT_ROOT
mkdir -p $LIBTORRENT_ROOT
pushd $LIBTORRENT_ROOT
svn checkout svn://svn.code.sf.net/p/libtorrent/code/branches/$LIBTORRENT_VERSION libtorrent-$LIBTORRENT_VERSION
popd

# Build BOOST and copy the .a files to LIBTORRENT_LIBS
pushd $BOOST_ROOT
sh ./bootstrap.sh
rm -rf bin*
rm -rf linux
$BOOST_ROOT/b2 variant=release link=static --stagedir=linux stage cxxflags="-O2 -fPIC" --with-date_time --with-chrono --with-system --with-random --with-thread
cp linux/lib/*.a $LIBTORRENT_LIBS
popd

# Build and copy the libtorrent .a files
pushd $LIBTORRENT_ROOT
$BOOST_ROOT/bjam toolset=gcc cxxflags="-O2 -fPIC" cflags="-fPIC" variant=release link=static deprecated-functions=off logging=none encryption=openssl boost=source
cp bin/gcc-$GCC_VERSION/release/boost-source/deprecated-functions-off/encryption-openssl/link-static/threading-multi/libtorrent.a $LIBTORRENT_LIBS/
popd

# Run the build_linux script....
echo "If there were no errors building BOOST and libtorrent proceed now to execute:"
echo "./build/build_linux.sh"
echo ""
