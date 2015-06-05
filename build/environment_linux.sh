#!/bin/bash
###############################################################################################################
# environment_linux.sh: define variables, paths and common functions for configure_linux.sh and build_linux.sh
###############################################################################################################

export GCC_VERSION=`gcc --version | sed -n 's/.*\([0-9].[0-9].[0-9]\)/\1/p'`

# Boost Version
export BOOST_VERSION_MAJOR="1"
export BOOST_VERSION_MINOR="58"
export BOOST_VERSION_BUILD="0"

export BOOST_UNDERSCORED_VERSION="${BOOST_VERSION_MAJOR}_${BOOST_VERSION_MINOR}_${BOOST_VERSION_BUILD}"
export BOOST_DOTTED_VERSION="${BOOST_VERSION_MAJOR}.${BOOST_VERSION_MINOR}.${BOOST_VERSION_BUILD}"

# Libtorrent version
export LIBTORRENT_VERSION="RC_1_0"

# Source Paths for BOOST and libtorrent
export BOOST_ROOT="$HOME/src/boost_$BOOST_UNDERSCORED_VERSION"
export LIBTORRENT_ROOT="$HOME/src/libtorrent-$LIBTORRENT_VERSION"

# (All binary libraries required from BOOST and libtorrent will go here for linking)
export LIBTORRENT_LIBS="$HOME/libs/LIBTORRENT_LIBS"

# Java/jni include folders.
export JDK_INCLUDE_1=$JAVA_HOME/include
export JDK_INCLUDE_2=$JAVA_HOME/include/linux

###############################################################################################################

function printEnvironment() {
  echo HOME=$HOME
  echo GCC_VERSION=$GCC_VERSION
  echo ""
  echo BOOST_UNDERSCORED_VERSION=$BOOST_UNDERSCORED_VERSION
  echo BOOST_DOTTED_VERSION=$BOOST_DOTTED_VERSION
  echo ""
  echo LIBTORRENT_VERSION=$LIBTORRENT_VERSION
  echo ""
  echo BOOST_ROOT=$BOOST_ROOT
  echo LIBTORRENT_ROOT=$LIBTORRENT_ROOT
  echo LIBTORRENT_LIBS=$LIBTORRENT_LIBS
  echo ""
  echo JAVA_HOME=$JAVA_HOME
  echo JDK_INCLUDE_1=$JAVA_HOME/include
  echo JDK_INCLUDE_2=$JAVA_HOME/include/linux
  echo ""
  echo CXXFLAGS=$CXXFLAGS
}

function enterToContinueOrAbort() {
  echo "(Press [Enter] to continue or Ctrl-c to abort)"
  echo ""
  read
}

function sanityCheck() {
  printEnvironment
  echo ""
  echo "Make sure the environment variables above are correct before proceeding forward."
  enterToContinueOrAbort
}
