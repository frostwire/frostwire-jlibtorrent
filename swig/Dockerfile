FROM ubuntu:18.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt update -y
# Common Java tooling
RUN apt install -y openjdk-11-jdk-headless
RUN apt install -y gradle
RUN apt install -y swig
# Common tooling
RUN apt install -y wget
RUN apt install -y unzip
RUN apt install -y less
RUN apt install -y python
RUN apt install -y perl
RUN apt install -y make
RUN apt install -y cmake
RUN apt install -y g++
RUN apt install -y git
RUN apt install -y ssh
# Windows toolchain
RUN apt install -y g++-mingw-w64-x86-64
# Linux Toolchain
RUN apt install -y g++-7
RUN apt install -y g++-7-multilib
# For testing and debugging
RUN apt install -y emacs-nox

ENV LIBTORRENT_REVISION="RC_1_2_ad2a6d7"
ENV BOOST_MAJOR="1"
ENV BOOST_VERSION="73"
ENV BOOST_MINOR="0"
ENV BOOST_DOT_VERSION="${BOOST_MAJOR}.${BOOST_VERSION}.${BOOST_MINOR}"
ENV BOOST_UNDERSCORE_VERSION="${BOOST_MAJOR}_${BOOST_VERSION}_${BOOST_MINOR}"
ENV BOOST_ROOT="/src/boost_${BOOST_UNDERSCORE_VERSION}"
#TODO: r21e (Jan 2021)
ENV NDK_VERSION="r21d"
ENV OPENSSL_VERSION="1.1.1l"
ENV OPENSSL_NO_OPTS="no-afalgeng no-async no-autoalginit no-autoerrinit no-capieng no-cms no-comp no-deprecated no-dgram no-dso no-dtls no-dynamic-engine no-egd no-engine no-err no-filenames no-gost no-hw no-makedepend no-multiblock no-nextprotoneg no-posix-io no-psk no-rdrand no-sctp no-shared no-sock no-srp no-srtp no-static-engine no-stdio no-threads no-ui-console no-zlib no-zlib-dynamic -fno-strict-aliasing -fvisibility=hidden -Os"

# build scripts don't stop
ENV DONT_PRESS_ANY_KEY 1

# third party SRC folder
RUN mkdir src

# prepare_boost
RUN cd /src \
		&& wget -nv -O boost.tar.gz https://dl.frostwire.com/other/boost/boost_${BOOST_UNDERSCORE_VERSION}.tar.gz \
    && tar xzf boost.tar.gz \
    && cd boost_${BOOST_UNDERSCORE_VERSION} \
    && ./bootstrap.sh

# download android NDK
RUN cd /src \
    && wget -nv -O android-ndk.zip https://dl.google.com/android/repository/android-ndk-${NDK_VERSION}-linux-x86_64.zip \
    && unzip -qq android-ndk.zip \
    && mv android-ndk-${NDK_VERSION} android-ndk

# Download OpenSSL
RUN cd /src \
    && wget -nv -O openssl.tar.gz https://www.openssl.org/source/openssl-${OPENSSL_VERSION}.tar.gz \
    && tar xzf openssl.tar.gz \
    && mv openssl-${OPENSSL_VERSION} openssl-src

# Make android toolchains
RUN cd /src \
    && android-ndk/build/tools/make_standalone_toolchain.py --arch arm --api 24 --stl libc++ --install-dir android-toolchain-arm \
    && android-ndk/build/tools/make_standalone_toolchain.py --arch arm64 --api 24 --stl libc++ --install-dir android-toolchain-arm64 \
    && android-ndk/build/tools/make_standalone_toolchain.py --arch x86 --api 24 --stl libc++ --install-dir android-toolchain-x86 \
    && android-ndk/build/tools/make_standalone_toolchain.py --arch x86_64 --api 24 --stl libc++ --install-dir android-toolchain-x86_64

# Build OpenSSL arm
RUN export CC=/src/android-toolchain-arm/bin/arm-linux-androideabi-clang \
    && export PATH=/src/android-toolchain-arm/arm-linux-androideabi/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-armv4 ${OPENSSL_NO_OPTS} -march=armv7-a -mfpu=neon --prefix=/openssl-arm \
    && make clean \
    && make \
    && make install_sw \
    && cd /

# Build OpenSSL arm64
RUN export CC=/src/android-toolchain-arm64/bin/aarch64-linux-android-clang \
    && export PATH=/src/android-toolchain-arm64/aarch64-linux-android/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-aarch64 ${OPENSSL_NO_OPTS} -march=armv8-a+crypto -mfpu=neon --prefix=/openssl-arm64 \
    && make clean \
    && make \
    && make install_sw \
    && cd /

# Build OpenSSL x86
RUN export CC=/src/android-toolchain-x86/bin/i686-linux-android-clang \
    && export PATH=/src/android-toolchain-x86/i686-linux-android/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-elf ${OPENSSL_NO_OPTS} --prefix=/openssl-x86 \
    && make clean \
    && make \
    && make install_sw \
    && cd /

# Build OpenSSL x86_64
RUN export CC=/src/android-toolchain-x86_64/bin/x86_64-linux-android-clang \
    && export PATH=/src/android-toolchain-x86_64/x86_64-linux-android/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-x86_64 ${OPENSSL_NO_OPTS} --prefix=/openssl-x86_64 \
    && make clean \
    && make \
    && make install_sw \
    && cd ..

#Build OpenSSL for Windows x86_64
RUN cd /src \
    && mkdir windows \
    && tar xvfz openssl.tar.gz --directory windows \
    && cd /src/windows/openssl-${OPENSSL_VERSION} \
    && sed -i 's/if defined(_WIN32_WINNT) && _WIN32_WINNT>=0x0333/if 0/g' crypto/cryptlib.c \
    && sed -i 's/MessageBox.*//g' crypto/cryptlib.c \
    && sed -i 's/return return 0;/return 0;/g' crypto/threads_none.c \
    && export CC=export CC=x86_64-w64-mingw32-gcc-posix \
    && ./Configure mingw64 ${OPENSSL_NO_OPTS} --prefix=/openssl-windows-x86_64 \
    && make clean \
    && make \
    && make install_sw \
    && cd /openssl-windows-x86_64/lib \
    && cp libcrypto.a libcrypto.lib \
    && cp libssl.a libssl.lib

#Build OpenSSL for Linux x86_64
RUN export CC=gcc-7 \
    && cd /src/openssl-src \
    && ./Configure linux-x86_64 ${OPENSSL_NO_OPTS} -fPIC --prefix=/openssl-linux-x86_64 \
    && make clean \
    && make \
    && make install_sw

# Make clean
RUN cd /src/openssl-src \
    && make clean \
    && cd ..

# Prepare latest version of libtorrent
RUN rm -fr /src/libtorrent
RUN cd /src \
    && git clone https://github.com/gubatron/libtorrent \
    && cd libtorrent \
    && git fetch origin RC_1_2 \
    && git reset --hard ${LIBTORRENT_REVISION} \
    && git submodule init \
    && git submodule update \
    && cd /

# Android cleanup (maybe not needed for jlibtorrent)

# toolchain arm

RUN sed -i 's/RANLIB = ranlib/RANLIB = "\/src\/android-toolchain-x86_64\/bin\/x86_64-linux-android-ranlib"/g' /src/boost_${BOOST_UNDERSCORE_VERSION}/tools/build/src/tools/gcc.jam
RUN mkdir frostwire-jlibtorrent
RUN echo "This folder will be mounted dynamically from the base of our repo every time the container is invoked via 'docker run'" > /frostwire-jlibtorrent/README.txt

# [BUILD DESKTOP ARCHITECTURES]
RUN echo "cd /src/libtorrent && git checkout -- . && git status . && cd /frostwire-jlibtorrent/swig && ./build-windows-x86_64.sh && ./build-linux-x86_64.sh" > build_desktop.sh
RUN chmod +x /build_desktop.sh

# [PATCH for Android: Turns off NETLINK socket in favor of IFADDR sockets]
ADD ifaddr.1.2.14.patch /src/libtorrent/ifaddr.patch

# [BUILD ANDROID EMULATOR ARCHITECTURE ONLY (x86)]
RUN echo "cd /src/libtorrent && git checkout -- . && patch -p1 < ifaddr.patch && cd /frostwire-jlibtorrent/swig && ./build-android-x86.sh" > build_android_x86.sh
RUN chmod +x /build_android_x86.sh

# [BUILD ALL ANDROID ARCHITECTURES]
RUN echo "cd /src/libtorrent && git checkout -- . && patch -p1 < ifaddr.patch && cd /frostwire-jlibtorrent/swig && ./build-android-arm.sh && ./build-android-arm64.sh && ./build-android-x86.sh && ./build-android-x86_64.sh" > build_android_all.sh
RUN chmod +x /build_android_all.sh
