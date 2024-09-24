# Installs dependencies and tools to build on macos

brew install wget
brew install pcre2

export SWIG_VERSION=4.2.1


# Function to check if SWIG is installed and return the version
check_swig_installed() {
  if command -v swig >/dev/null 2>&1; then
    INSTALLED_VERSION=$(swig -version | grep "SWIG Version" | awk '{print $3}')
    if [ "$INSTALLED_VERSION" == "$SWIG_VERSION" ]; then
      echo "SWIG version $SWIG_VERSION is already installed."
      return 0
    else
      echo "SWIG version $INSTALLED_VERSION is installed, but we need version $SWIG_VERSION."
      return 1
    fi
  else
    echo "SWIG is not installed."
    return 1
  fi
}

# Check if the correct SWIG version is installed
if ! check_swig_installed; then
  echo "Proceeding to download and install SWIG version $SWIG_VERSION..."
  
  if [ ! -d swig-${SWIG_VERSION} ]; then
    wget -nv --no-check-certificate https://dl.frostwire.com/other/swig/swig-${SWIG_VERSION}.tar.gz
    tar xzf swig-${SWIG_VERSION}.tar.gz
  fi
  
  cd swig-${SWIG_VERSION}
  ./configure --without-d
  make -j 8
  sudo make install
  
  # Recheck SWIG installation after build and install
  if check_swig_installed; then
    echo "SWIG version $SWIG_VERSION has been successfully installed."
  else
    echo "There was an issue installing SWIG version $SWIG_VERSION."
  fi
else
  echo "No need to reinstall SWIG."
fi


