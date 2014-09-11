#!/bin/bash
#exit on error
set -e 
#print commands
set -x 

LINK_LIBS="-ltorrent-rasterbar -lboost_system -lboost_filesystem"
STD="-std=c++11"

if [[ "$OSTYPE" == "darwin"* ]]; then
  CC=clang++
  STD_LIB="-stdlib=libc++"
  INCLUDES="-I/usr/local/libtorrent-rasterbar/1.0.1_1/include"
  LINKER_PATHS="-L/usr/local/Cellar/boost/HEAD/lib"
elif [[ "$OSTYPE" == "linux-gnu" ]]; then
  CC=g++
  STD_LIB=""
  INCLUDES="-I/usr/local/includes"
  LINKER_PATHS="-L/usr/local/lib"
  export LD_LIBRARY_PATH="/usr/local/lib"
fi

CPP_FILES="api_walkthrough.cpp"
OUTPUT="api_walkthrough"

FLAGS="-Wfatal-errors ${STD} ${STD_LIB} -DBOOST_ASIO_SEPARATE_COMPILATION -g"

${CC} ${FLAGS} ${INCLUDES} ${CPP_FILES} -o ${OUTPUT} ${LINK_LIBS} ${LINKER_PATHS}

