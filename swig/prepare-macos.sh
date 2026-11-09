# Installs dependencies and tools to build on macos
brew install wget
brew install pcre2
brew install automake
brew install autoconf
# DO NOT USE XTool's bison, the swig build uses a -W flag that's not recognized by it
brew install bison
# CMake builds usrsctp and libjuice for WebTorrent (libtorrent/deps/libdatachannel)
command -v cmake >/dev/null || brew install cmake
export PATH="$(brew --prefix bison)/bin:${PATH}"

# The SWIG version and the sha256 of its tarball come from build-utils.shinc, the one place the
# build pins them (the Dockerfile and the release workflow use the same values).
SWIG_VERSION=$(sed -n 's/^ *export SWIG_VERSION="\(.*\)"/\1/p' build-utils.shinc)
SWIG_SHA256=$(sed -n 's/^ *export SWIG_SHA256="\([0-9a-f]*\)".*/\1/p' build-utils.shinc)
if [ -z "${SWIG_VERSION}" ] || [ -z "${SWIG_SHA256}" ]; then
  echo "prepare-macos.sh: run it from the swig folder (SWIG_VERSION/SWIG_SHA256 not found in build-utils.shinc)"
  exit 1
fi
export SWIG_VERSION

# Function to check if SWIG is installed and return the version
check_swig_installed() {
  # Find the path to the swig executable.
  swig_path=$(command -v swig 2>/dev/null)

  # Check if a valid path was found and the file exists.
  if [ -n "$swig_path" ] && [ -x "$swig_path" ]; then
    INSTALLED_VERSION=$("$swig_path" -version | grep "SWIG Version" | awk '{print $3}')

    if [ "$INSTALLED_VERSION" == "$SWIG_VERSION" ]; then
      echo "SWIG version $SWIG_VERSION is already installed."
      return 0
    else
      echo "SWIG version $INSTALLED_VERSION is installed, but we need version $SWIG_VERSION."
      return 1
    fi
  else
    echo "SWIG is not installed or the path is invalid."
    return 1
  fi
}

# Check if the correct SWIG version is installed
if ! check_swig_installed; then
  echo "Proceeding to download and install SWIG version $SWIG_VERSION..."
  
  if [ ! -d "swig-${SWIG_VERSION}" ]; then
    wget -nv https://dl.frostwire.com/other/swig/swig-${SWIG_VERSION}.tar.gz
    echo "${SWIG_SHA256}  swig-${SWIG_VERSION}.tar.gz" | shasum -a 256 -c - || exit 1
    tar xzf "swig-${SWIG_VERSION}.tar.gz"
  fi
  
  if [ -d "swig-${SWIG_VERSION}" ]; then
    cd "swig-${SWIG_VERSION}" || { echo "Failed to change directory to swig-${SWIG_VERSION}"; exit 1; }
    ./autogen.sh
    ./configure --without-d
    make -j 8
    sudo make install
    
    cd ..
  else
    echo "Error: Source directory swig-${SWIG_VERSION} not found after extraction."
    exit 1
  fi

  # Add /usr/local/bin to PATH for the current shell session
  export PATH="/usr/local/bin:$PATH"  
  
  # Recheck SWIG installation after build and install
  if check_swig_installed; then
    echo "SWIG version $SWIG_VERSION has been successfully installed."
  else
    echo "There was an issue installing SWIG version $SWIG_VERSION."
  fi
else
  echo "No need to reinstall SWIG."
fi
