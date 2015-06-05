#!/bin/bash
source ./build/environment_linux.sh
sanityCheck


function installDependencies() {
  # TODO: Make this work for multiple distributions as users ask for it. support rpm packages, etc.
  echo "About to ask you for sudo permission to perform: sudo apt-get install libbz2-dev libssl1.0.0 libcrypto++9 subversion"
  enterToContinueOrAbort
  sudo apt-get install libbz2-dev libssl1.0.0 libcrypto++9 subversion
}

function downloadBOOST() {
  mkdir -p $BOOST_ROOT
  pushd $BOOST_ROOT/..
  wget -O boost_$BOOST_UNDERSCORED_VERSION.tar.bz2 http://sourceforge.net/projects/boost/files/boost/$BOOST_DOTTED_VERSION/boost_$BOOST_UNDERSCORED_VERSION.tar.bz2/download
  tar -xvf boost_$BOOST_UNDERSCORED_VERSION.tar.bz2
  popd
}

function downloadLibtorrent() {
  mkdir -p $LIBTORRENT_ROOT
  mkdir -p $LIBTORRENT_LIBS
  pushd $LIBTORRENT_ROOT/..
  svn checkout svn://svn.code.sf.net/p/libtorrent/code/branches/$LIBTORRENT_VERSION libtorrent-$LIBTORRENT_VERSION
  popd
}

function buildBoost() {
  pushd $BOOST_ROOT
  sh ./bootstrap.sh
  rm -fr bin*
  rm -fr linux
  $BOOST_ROOT/b2 variant=release link=static --stagedir=linux stage cxxflags="-O2 -fPIC" --with-date_time --with-chrono --with-system --with-random --with-thread
  cp linux/lib/*.a $LIBTORRENT_LIBS
  popd
}

# Build and copy the libtorrent .a files
function buildLibtorrent() {
  pushd $LIBTORRENT_ROOT
  rm -fr bin*  
  $BOOST_ROOT/bjam toolset=gcc cxxflags="-O2 -fPIC" cflags="-fPIC" variant=release link=static deprecated-functions=off logging=none encryption=openssl boost=source
  cp bin/gcc-$GCC_VERSION/release/boost-source/deprecated-functions-off/encryption-openssl/link-static/threading-multi/libtorrent.a $LIBTORRENT_LIBS/
  popd
}

confirmAndExecute "Want to install debian/ubuntu dependencies?" installDependencies
confirmAndExecute "Want to download BOOST $BOOST_DOTTED_VERSION?" downloadBOOST
confirmAndExecute "Want to build BOOST $BOOST_DOTTED_VERSION?" buildBoost
confirmAndExecute "Want to download libtorrent $LIBTORRENT_VERSION?" downloadLibtorrent
confirmAndExecute "Want to build libtorrent $LIBTORRENT_VERSION?" buildLibtorrent
confirmAndExecute "Want to build libtorrent?" buildLibtorrent

# Run the build_linux script....
echo ""
echo "If there were no errors building BOOST and libtorrent proceed now to execute:"
echo "./build/build_linux.sh"
echo ""
