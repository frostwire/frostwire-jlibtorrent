#!/bin/bash

function runJni()
{
    JAVA_SRC_OUTPUT=../src/main/java/com/frostwire/jlibtorrent/swig

    rm -rf ${JAVA_SRC_OUTPUT}
    mkdir -p ${JAVA_SRC_OUTPUT}

    swig -c++ -java -o libtorrent_jni.cpp \
        -outdir ${JAVA_SRC_OUTPUT} \
        -package com.frostwire.jlibtorrent.swig \
        -DLIBTORRENT_SWIG_JNI \
        -I${BOOST_ROOT} \
        -I${LIBTORRENT_ROOT}/include \
        -DBOOST_ASIO_DECL="" \
        -DBOOST_NO_TYPEID=1 \
        -DBOOST_NO_EXCEPTIONS \
        -DBOOST_POSIX_API=1 \
        -DBOOST_SYSTEM_NOEXCEPT="" \
        -DBOOST_SYSTEM_DECL="" \
        -DBOOST_SYSTEM_NO_DEPRECATED=1 \
        -DTORRENT_CFG="TORRENT_CFG" \
        -DTORRENT_NO_DEPRECATE=1 \
        -DTORRENT_DEPRECATED_EXPORT="" \
        -DTORRENT_EXPORT="" \
        -DTORRENT_EXTRA_EXPORT="" \
        -DTORRENT_USE_IPV6=1 \
        -DTORRENT_DISABLE_GEO_IP=1 \
        -DTORRENT_USE_BOOST_DATE_TIME=1 \
        -DTORRENT_USE_OPENSSL=1 \
        -DTORRENT_EXCEPTION_THROW_SPECIFIER=noexcept \
        -DTORRENT_NO_RETURN="" \
        -DTORRENT_OVERRIDE="" \
        -DTORRENT_FINAL="" \
        -DTORRENT_DEBUG_REFCOUNTS=1 \
        -DTORRENT_FORMAT\(x,y\)="" \
        -DTORRENT_DISK_STATS=1 \
        -DTORRENT_UTP_LOG_ENABLE=1 \
        -DNDEBUG=1 \
        -D_LIBTORRENT_REVISION_SHA1_="\"$(git -C $LIBTORRENT_ROOT rev-list HEAD | head -1)\"" \
        -D_JLIBTORRENT_REVISION_SHA1_="\"$(git rev-list HEAD | head -1)\"" \
        libtorrent.i

    # at first sigh, this could look like a very dangerous thing to
    # do, but in practice, these director types are controlled by us
    # and we know we can do it. The main reason is to be able to
    # compile with -fno-rtti.
    sed -i '' 's/dynamic_cast<SwigDirector_/static_cast<SwigDirector_/g' libtorrent_jni.cpp
}

function runNode()
{
    swig -c++ -javascript -node -o libtorrent_node.cpp \
        -DLIBTORRENT_SWIG_NODE \
        -DV8_VERSION=0x040685 \
        -I${BOOST_ROOT} \
        -I${LIBTORRENT_ROOT}/include \
        -DBOOST_ASIO_DECL="" \
        -DBOOST_NO_TYPEID=1 \
        -DBOOST_NO_EXCEPTIONS \
        -DBOOST_POSIX_API=1 \
        -DBOOST_SYSTEM_NOEXCEPT="" \
        -DBOOST_SYSTEM_DECL="" \
        -DBOOST_SYSTEM_NO_DEPRECATED=1 \
        -DTORRENT_CFG="TORRENT_CFG" \
        -DTORRENT_NO_DEPRECATE=1 \
        -DTORRENT_DEPRECATED_EXPORT="" \
        -DTORRENT_EXPORT="" \
        -DTORRENT_EXTRA_EXPORT="" \
        -DTORRENT_USE_IPV6=1 \
        -DTORRENT_DISABLE_GEO_IP=1 \
        -DTORRENT_USE_BOOST_DATE_TIME=1 \
        -DTORRENT_USE_OPENSSL=1 \
        -DTORRENT_EXCEPTION_THROW_SPECIFIER=noexcept \
        -DTORRENT_NO_RETURN="" \
        -DTORRENT_OVERRIDE="" \
        -DTORRENT_FINAL="" \
        -DTORRENT_DEBUG_REFCOUNTS=1 \
        -DTORRENT_FORMAT\(x,y\)="" \
        -DTORRENT_DISK_STATS=1 \
        -DTORRENT_UTP_LOG_ENABLE=1 \
        -DNDEBUG=1 \
        -D_LIBTORRENT_REVISION_SHA1_="\"$(git -C $LIBTORRENT_ROOT rev-list HEAD | head -1)\"" \
        -D_JLIBTORRENT_REVISION_SHA1_="\"$(git rev-list HEAD | head -1)\"" \
        libtorrent.i
}

function usage() {
    echo "run-swig.sh Create the swig wrappers on the native code based on the instructions of libtorrent.i"
    echo ""
    echo "Usage: ./run-swigh.sh [-jni -node]"
    echo ""
    echo ""
    echo "Options:"
    echo ""
    echo "-h,--help    Show this help."
    echo "--jni        Only create JNI wrappers .cpp sources."
    echo "--node       Only create NodeJS wrappers .cpp sources."
    echo ""
}

if [ "$1" == "--help" -o "$1" == "-h" ]; then
    usage;
    exit 1;
elif [ "$1" == "--jni" ]; then
    runJni;
    exit 1;
elif [ "$1" == "--node" ]; then
    runNode;
    exit 1;
else

    echo "Are you sure you want to create wrappers for"
    echo "    JNI (Java)"
    echo "    Node (Javascript)"
    echo ""
    echo "(Press [y] to continue, or any other key to abort. Press [^C] and './run-swig.sh -h' for help)"

    read buildAll

    if echo $buildAll | grep -q "^[Yy]" ; then
        runJni
        runNode
    fi
fi

