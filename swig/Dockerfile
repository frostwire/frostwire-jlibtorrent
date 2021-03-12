FROM ubuntu:18.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt update -y
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
RUN apt install -y swig
RUN apt install -y openjdk-11-jdk-headless
RUN apt install -y gradle

ENV BOOST_MAJOR="1"
ENV BOOST_VERSION="73"
ENV BOOST_MINOR="0"
ENV BOOST_DOT_VERSION="${BOOST_MAJOR}.${BOOST_VERSION}.${BOOST_MINOR}"
ENV BOOST_UNDERSCORE_VERSION="${BOOST_MAJOR}_${BOOST_VERSION}_${BOOST_MINOR}"
ENV BOOST_ROOT="/src/boost_${BOOST_UNDERSCORE_VERSION}"
ENV NDK_VERSION="r21d"
ENV OPENSSL_VERSION="1.1.1j"
ENV OPENSSL_NO_OPTS="no-afalgeng no-async no-autoalginit no-autoerrinit no-capieng no-cms no-comp no-deprecated no-dgram no-dso no-dtls no-dynamic-engine no-egd no-engine no-err no-filenames no-gost no-hw no-makedepend no-multiblock no-nextprotoneg no-posix-io no-psk no-rdrand no-sctp no-shared no-sock no-srp no-srtp no-static-engine no-stdio no-threads no-ui-console no-zlib no-zlib-dynamic -fno-strict-aliasing -fvisibility=hidden -Os"

# build scripts don't stop
ENV DONT_PRESS_ANY_KEY 1

# third party SRC folder
RUN mkdir src

# prepare_boost
RUN cd /src \
    && wget -nv -O boost.tar.gz https://dl.bintray.com/boostorg/release/${BOOST_DOT_VERSION}/source/boost_${BOOST_UNDERSCORE_VERSION}.tar.gz \
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
    && android-ndk/build/tools/make_standalone_toolchain.py --arch arm --api 19 --stl libc++ --install-dir android-toolchain-arm \
    && android-ndk/build/tools/make_standalone_toolchain.py --arch arm64 --api 21 --stl libc++ --install-dir android-toolchain-arm64 \
    && android-ndk/build/tools/make_standalone_toolchain.py --arch x86 --api 19 --stl libc++ --install-dir android-toolchain-x86 \
    && android-ndk/build/tools/make_standalone_toolchain.py --arch x86_64 --api 21 --stl libc++ --install-dir android-toolchain-x86_64

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

# Make clean
RUN cd /src/openssl-src \
    && make clean \
    && cd ..

# Android cleanup (maybe not needed for jlibtorrent)
RUN sed -i 's/POSIX_FADV_RANDOM 1/POSIX_FADV_RANDOM_NOOP 0/g' /src/android-toolchain-arm/sysroot/usr/include/linux/fadvise.h
RUN sed -i 's/24/19/g' /src/android-toolchain-arm/sysroot/usr/include/stdio.h
RUN sed -i 's/24/19/g' /src/android-toolchain-arm/sysroot/usr/include/ifaddrs.h
RUN sed -i 's/28/19/g' /src/android-toolchain-arm/sysroot/usr/include/sys/random.h
RUN sed -i 's/24/21/g' /src/android-toolchain-arm64/sysroot/usr/include/stdio.h
RUN sed -i 's/24/21/g' /src/android-toolchain-arm64/sysroot/usr/include/ifaddrs.h
RUN sed -i 's/28/21/g' /src/android-toolchain-arm64/sysroot/usr/include/sys/random.h
RUN sed -i 's/POSIX_FADV_RANDOM 1/POSIX_FADV_RANDOM_NOOP 0/g' /src/android-toolchain-x86/sysroot/usr/include/linux/fadvise.h
RUN sed -i 's/24/19/g' /src/android-toolchain-x86/sysroot/usr/include/stdio.h
RUN sed -i 's/24/19/g' /src/android-toolchain-x86/sysroot/usr/include/ifaddrs.h
RUN sed -i 's/28/19/g' /src/android-toolchain-x86/sysroot/usr/include/sys/random.h
RUN sed -i 's/24/21/g' /src/android-toolchain-x86_64/sysroot/usr/include/stdio.h
RUN sed -i 's/24/21/g' /src/android-toolchain-x86_64/sysroot/usr/include/ifaddrs.h
RUN sed -i 's/28/21/g' /src/android-toolchain-x86_64/sysroot/usr/include/sys/random.h
RUN sed -i 's/RANLIB = ranlib/RANLIB = "\/src\/android-toolchain-x86_64\/bin\/x86_64-linux-android-ranlib"/g' /src/boost_${BOOST_UNDERSCORE_VERSION}/tools/build/src/tools/gcc.jam
RUN mkdir frostwire-jlibtorrent
RUN echo "This folder will be mounted dynamically from the base of our repo every time the container is invoked via 'docker run'" > /frostwire-jlibtorrent/README.txt
RUN echo "cd /frostwire-jlibtorrent/swig && ./build-android-arm64.sh" > /build.sh
RUN chmod +x /build.sh
