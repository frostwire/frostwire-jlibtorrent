FROM --platform=linux/amd64 ubuntu:18.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt update -y
# Common Java tooling
RUN apt install -y openjdk-11-jdk-headless
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
RUN apt install -y build-essential
RUN apt install -y libtool
RUN apt install -y libpcre3-dev
RUN apt install -y libpcre3
# Windows toolchain
RUN apt install -y g++-mingw-w64-x86-64
# Linux Toolchain
RUN apt install -y g++-7
RUN apt install -y g++-7-multilib
# For testing and debugging
RUN apt install -y emacs-nox

ENV LIBTORRENT_REVISION="RC_1_2_239500acf2bc60dcb91330bf3c21b939d9f603af"
ENV SWIG_VERSION="4.0.2"
ENV BOOST_MAJOR="1"
ENV BOOST_VERSION="79"
ENV BOOST_MINOR="0"
ENV BOOST_DOT_VERSION="${BOOST_MAJOR}.${BOOST_VERSION}.${BOOST_MINOR}"
ENV BOOST_UNDERSCORE_VERSION="${BOOST_MAJOR}_${BOOST_VERSION}_${BOOST_MINOR}"
ENV BOOST_ROOT="/src/boost_${BOOST_UNDERSCORE_VERSION}"
ENV NDK_VERSION="r23"
ENV ANDROID_API="24"
ENV OPENSSL_VERSION="1.1.1n"
ENV OPENSSL_NO_OPTS="no-afalgeng no-async no-autoalginit no-autoerrinit no-capieng no-cms no-comp no-deprecated no-dgram no-dso no-dtls no-dynamic-engine no-egd no-engine no-err no-filenames no-gost no-hw no-makedepend no-multiblock no-nextprotoneg no-posix-io no-psk no-rdrand no-sctp no-shared no-sock no-srp no-srtp no-static-engine no-stdio no-threads no-ui-console no-zlib no-zlib-dynamic -fno-strict-aliasing -fvisibility=hidden -Os"
ENV GRADLE_VERSION="7.3.1"
ENV GRADLE_HOME="/src/gradle-${GRADLE_VERSION}"

# build scripts don't stop
ENV DONT_PRESS_ANY_KEY 1
ENV PATH=${GRADLE_HOME}/bin:${PATH}

# third party SRC folder
RUN mkdir src

# prepare gradle
RUN cd /src \
    && wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip

# prepare swig
RUN cd /src \
		&& wget -nv https://dl.frostwire.com/other/swig/swig-${SWIG_VERSION}.tar.gz \
		&& tar xzf swig-${SWIG_VERSION}.tar.gz \
		&& cd /src/swig-${SWIG_VERSION} \
		&& ./configure \
		&& make -j 8 \
    && make install

RUN swig -version

# prepare_boost
RUN cd /src \
    && wget -nv -O boost.tar.gz https://boostorg.jfrog.io/artifactory/main/release/${BOOST_DOT_VERSION}/source/boost_${BOOST_UNDERSCORE_VERSION}.tar.gz \
    && tar xzf boost.tar.gz \
    && cd boost_${BOOST_UNDERSCORE_VERSION} \
    && ./bootstrap.sh

# download android NDK
RUN cd /src \
    && wget -nv -O android-ndk-linux.zip https://dl.google.com/android/repository/android-ndk-${NDK_VERSION}-linux.zip \
    && unzip -qq android-ndk-linux.zip \
    && mv android-ndk-${NDK_VERSION} android-ndk

# Download OpenSSL
RUN cd /src \
    && wget -nv -O openssl.tar.gz https://www.openssl.org/source/openssl-${OPENSSL_VERSION}.tar.gz \
    && tar xzf openssl.tar.gz \
    && mv openssl-${OPENSSL_VERSION} openssl-src

ENV ANDROID_TOOLCHAIN="/src/android-ndk/toolchains/llvm/prebuilt/linux-x86_64"
ENV AR="${ANDROID_TOOLCHAIN}/bin/llvm-ar"
ENV LD="${ANDROID_TOOLCHAIN}/bin/ld"
ENV RANLIB="${ANDROID_TOOLCHAIN}/bin/llvm-ranlib"

# Build OpenSSL Android arm
RUN export CC=${ANDROID_TOOLCHAIN}/bin/armv7a-linux-androideabi${ANDROID_API}-clang \
    && export PATH=${ANDROID_TOOLCHAIN}/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-armv4 ${OPENSSL_NO_OPTS} -march=armv7-a -mfpu=neon --prefix=/openssl-android-arm \
    && make clean \
    && make \
    && make install_sw \
    && cd /

# Build OpenSSL Android arm64
RUN export CC=${ANDROID_TOOLCHAIN}/bin/aarch64-linux-android${ANDROID_API}-clang \
    && export PATH=${ANDROID_TOOLCHAIN}/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-aarch64 ${OPENSSL_NO_OPTS} -march=armv8-a+crypto -mfpu=neon --prefix=/openssl-android-arm64 \
    && make clean \
    && make \
    && make install_sw \
    && cd /

# Build OpenSSL Android x86
RUN export CC=${ANDROID_TOOLCHAIN}/bin/i686-linux-android${ANDROID_API}-clang \
    && export PATH=${ANDROID_TOOLCHAIN}/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-elf ${OPENSSL_NO_OPTS} --prefix=/openssl-android-x86 \
    && make clean \
    && make \
    && make install_sw \
    && cd /

# Build OpenSSL Android x86_64
RUN export CC=${ANDROID_TOOLCHAIN}/bin/x86_64-linux-android${ANDROID_API}-clang \
    && export PATH=${ANDROID_TOOLCHAIN}/bin:${PATH} \
    && cd /src/openssl-src \
    && ./Configure linux-x86_64 ${OPENSSL_NO_OPTS} --prefix=/openssl-android-x86_64 \
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

RUN mkdir frostwire-jlibtorrent
RUN echo "This folder will be mounted dynamically from the base of our repo every time the container is invoked via 'docker run'" > /frostwire-jlibtorrent/README.txt

# BUILD DESKTOP ARCHITECTURES (Windows and Linux x86_64)
RUN echo "cd /src/libtorrent && git checkout -- . && git status . && cd /frostwire-jlibtorrent/swig && ./build-windows-x86_64.sh && ./build-linux-x86_64.sh" > build_desktop.sh
RUN chmod +x /build_desktop.sh

# SCRIPT BUILD ANDROID X86 ONLY (ANDROID STUDIO'S EMULATOR ARCHITECTURE)
RUN echo "cd /frostwire-jlibtorrent/swig && ./build-android-x86.sh" > build_android_x86.sh
RUN chmod +x /build_android_x86.sh

# SCRIPT TO BUILD ANDROID ARM ONLY (armv7)
RUN echo "cd /frostwire-jlibtorrent/swig && ./build-android-arm.sh" > build_android_arm.sh
RUN chmod +x /build_android_arm.sh

# SCRIPT TO BUILD ANDROID ARM64 ONLY (armv8)
RUN echo "cd /frostwire-jlibtorrent/swig && ./build-android-arm64.sh" > build_android_arm64.sh
RUN chmod +x /build_android_arm64.sh

# SCRIPT TO BUILD ALL ANDROID ARCHITECTURES
RUN echo "cd /frostwire-jlibtorrent/swig && ./build-android-arm.sh && ./build-android-arm64.sh && ./build-android-x86.sh && ./build-android-x86_64.sh" > build_android_all.sh
RUN chmod +x /build_android_all.sh

RUN ln -s /frostwire-jlibtorrent/swig/libtorrent_patches/apply_android_patches.sh
