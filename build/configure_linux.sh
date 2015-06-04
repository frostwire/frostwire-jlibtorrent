# Boost and libtorrent versions:
LIBTORRENT_VERSION="RC_1_0"
BOOST_VERSION="1_58_0"


# Some setup
sudo apt-get install libbz2-dev libssl1.0.0 libcrypto++9 subversion
mkdir $HOME/LIBTORRENT_LIBS
export LIBTORRENT_LIBS="$HOME/LIBTORRENT_LIBS"
#export CXXFLAGS="-stdlib=libc++ -std=c++11 -O3 -I$BOOST_ROOT"

# Download Libtorrent and Boost, and export the home folders
cd $LIBTORRENT_LIBS
wget -O boost_$BOOST_VERSION.tar.bz2 http://sourceforge.net/projects/boost/files/boost/1.58.0/boost_$BOOST_VERSION.tar.bz2/download
tar -xvf boost_$BOOST_VERSION.tar.bz2
svn checkout svn://svn.code.sf.net/p/libtorrent/code/branches/$LIBTORRENT_VERSION libtorrent-$LIBTORRENT_VERSION

# Set the exports
export LIBTORRENT_ROOT="$LIBTORRENT_LIBS/libtorrent-$LIBTORRENT_VERSION"
export BOOST_ROOT="$LIBTORRENT_LIBS/boost_$BOOST_VERSION"

# Build boost and copy the .a files
cd $BOOST_ROOT
sh ./bootstrap.sh
rm -rf bin*
rm -rf linux
$BOOST_ROOT/b2 variant=release link=static --stagedir=linux stage cxxflags="-O2 -fPIC" --with-date_time --with-chrono --with-system --with-random --with-thread
cp linux/lib/*.a $LIBTORRENT_LIBS

# Build and copy the libtorrent .a files
cd $LIBTORRENT_ROOT
$BOOST_ROOT/bjam toolset=gcc cxxflags="-O2 -fPIC" cflags="-fPIC" variant=release link=static deprecated-functions=off logging=none encryption=openssl boost=source
sudo cp bin/gcc-4.8/release/boost-source/deprecated-functions-off/encryption-openssl/link-static/threading-multi/libtorrent.a $LIBTORRENT_LIBS/

# Run the build_linux script....