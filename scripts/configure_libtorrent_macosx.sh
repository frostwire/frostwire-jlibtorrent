export CC=clang
export CXX=clang
export CFLAGS="-O3 -DTORRENT_USE_IPV6=1"
export CXXFLAGS=-O3

./configure \
    --enable-shared \
    --enable-static \
    --enable-debug=no \
    --disable-debug \
    --disable-deprecated-functions \
    --disable-encryption \
    --disable-geoip