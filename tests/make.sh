#!/bin/bash
#exit on error
set -e 
#print commands
set -x 
CC=g++

INCLUDES="-I/usr/local/includes/"
LINK_LIBS="-ltorrent-rasterbar -lboost_system -lboost_filesystem"
LINKER_PATHS="-L/usr/local/Cellar/boost/HEAD/lib"
STD="-std=c++11"
STD_LIB="-stdlib=libstdc++"

if [[ $OSTYPE == darwin* ]] ; then
  CC=clang++
  STD="-std=c++11"
  STD_LIB="-stdlib=libc++"
  INCLUDES="-I/usr/local/libtorrent-rasterbar/1.0.1_1/include"
fi

CPP_FILES="api_walkthrough.cpp"
OUTPUT="api_walkthrough"

FLAGS="-Wfatal-errors ${STD} ${STD_LIB} -DBOOST_ASIO_SEPARATE_COMPILATION -g"

${CC} ${FLAGS} ${INCLUDES} ${CPP_FILES} -o ${OUTPUT} ${LINK_LIBS} ${LINKER_PATHS}
