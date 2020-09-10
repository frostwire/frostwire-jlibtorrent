#!/bin/bash
# NOTE: Run this script manually every time you make changes to libtorrent.i, this is not ran by any of the build scripts, including .travis.yml
source build-utils.shinc

abort_if_var_unset "LIBTORRENT_ROOT" ${LIBTORRENT_ROOT}
abort_if_var_unset "BOOST_ROOT" ${BOOST_ROOT}

# Extracted from ../build.gradle
JLIBTORRENT_VERSION=`sed -n -e '/^version /s/.* //p' ../build.gradle | tr -d "'"`
abort_if_var_unset "JLIBTORRENT_VERSION" ${JLIBTORRENT_VERSION}

function fixCode() {
    uname=`uname -s`
    if [ "$(uname)" == "Darwin" ]; then
      # FreeBSD's sed is weird, it needs that extra '' parameter there for some reason
      sed -i '' 's/constexpr alert_category_t all = alert_category_t::all();/ \/\/deleted temporarily because it is defined twice/g' ${LIBTORRENT_ROOT}/include/libtorrent/alert.hpp
      # The line above will be reverted to normal when we build by the checkout, if not commented/deleted swig breaks
      sed -i '' 's/) &;/)  ;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i '' 's/) & noexcept;/)   noexcept;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i '' 's/(time_point32::min)();/time_point32::min();/g' ${LIBTORRENT_ROOT}/include/libtorrent/announce_entry.hpp
      sed -i '' 's/userdata = client_data_t{});/userdata);/g' ${LIBTORRENT_ROOT}/include/libtorrent/torrent_handle.hpp
    else
      sed -i 's/constexpr alert_category_t all = alert_category_t::all();/ \/\/deleted temporarily because it is defined twice/g' ${LIBTORRENT_ROOT}/include/libtorrent/alert.hpp
      sed -i 's/) &;/)  ;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i 's/) & noexcept;/)   noexcept;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i 's/(time_point32::min)();/time_point32::min();/g' ${LIBTORRENT_ROOT}/include/libtorrent/announce_entry.hpp
      sed -i 's/userdata = client_data_t{});/userdata);/g' ${LIBTORRENT_ROOT}/include/libtorrent/torrent_handle.hpp
    fi
}

function refixCode() {
    uname=`uname -s`    
    if [ "$(uname)" == "Darwin" ]; then   
      sed -i '' 's/)  ;/) \&;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i '' 's/)   noexcept;/) \& noexcept;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i '' 's/time_point32::min();/(time_point32::min)();/g' ${LIBTORRENT_ROOT}/include/libtorrent/announce_entry.hpp
      sed -i '' 's/userdata);/userdata = client_data_t{});/g' ${LIBTORRENT_ROOT}/include/libtorrent/torrent_handle.hpp      
    else
      sed -i 's/)  ;/) \&;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i 's/)   noexcept;/) \& noexcept;/g' ${LIBTORRENT_ROOT}/include/libtorrent/file_storage.hpp
      sed -i 's/time_point32::min();/(time_point32::min)();/g' ${LIBTORRENT_ROOT}/include/libtorrent/announce_entry.hpp
      sed -i 's/userdata);/userdata = client_data_t{});/g' ${LIBTORRENT_ROOT}/include/libtorrent/torrent_handle.hpp      
    fi
}

function runJni()
{
    JAVA_SRC_OUTPUT=../src/main/java/com/frostwire/jlibtorrent/swig
    rm -rf ${JAVA_SRC_OUTPUT}
    mkdir -p ${JAVA_SRC_OUTPUT}

    # If you're running from Ubuntu and you have swig 3.0.5 you might end up with a segfault running the swig command below
    #./run-swig.sh: line 33: 46204 Segmentation fault
    # Make sure your swig command is compiled from source, version 3.0.12 works on mac
    # https://sourceforge.net/projects/swig/files/swig/swig-3.0.12/swig-3.0.12.tar.gz/download
    
    swig -c++ -java -o libtorrent_jni.cpp \
        -outdir ${JAVA_SRC_OUTPUT} \
        -package com.frostwire.jlibtorrent.swig \
        -I${BOOST_ROOT} \
        -I${LIBTORRENT_ROOT}/include \
        -DBOOST_ASIO_DECL="" \
        -DBOOST_NO_TYPEID=1 \
        -DBOOST_NO_EXCEPTIONS \
        -DBOOST_POSIX_API=1 \
        -DBOOST_SYSTEM_CONSTEXPR="" \
        -DBOOST_SYSTEM_NOEXCEPT="" \
        -DBOOST_SYSTEM_DECL="" \
        -DBOOST_SYSTEM_NO_DEPRECATED=1 \
        -DBOOST_NO_IOSTREAM \
        -DBOOST_SYMBOL_VISIBLE \
        -DBOOST_NOEXCEPT="" \
        -DBOOST_NOEXCEPT_OR_NOTHROW="" \
        -DTORRENT_ABI_VERSION=2 \
        -DTORRENT_VERSION_NAMESPACE_2="" \
        -DTORRENT_VERSION_NAMESPACE_2_END="" \
        -DTORRENT_IPV6_NAMESPACE="" \
        -DTORRENT_IPV6_NAMESPACE_END="" \
        -DTORRENT_CFG="TORRENT_CFG" \
        -DTORRENT_NO_DEPRECATE \
        -DTORRENT_DEPRECATED_EXPORT="" \
        -DTORRENT_DEPRECATED_MEMBER="" \
        -DTORRENT_DEPRECATED_ENUM="" \
        -DTORRENT_DEPRECATED \
        -DTORRENT_EXPORT="" \
        -DTORRENT_EXTRA_EXPORT="" \
        -DTORRENT_FORMAT\(x,y\)="" \
        -DNDEBUG=1 \
        -D_bit="" \
        -Dfinal="" \
        -DBOOST_OVERRIDE="override" \
        libtorrent.i

    # at first sight, this could look like a very dangerous thing to
    # do, but in practice, these director types are controlled by us
    # and we know we can do it. The main reason is to be able to
    # compile with -fno-rtti.
    uname=`uname -s`
    if [ "$(uname)" == "Darwin" ]; then    
	sed -i '' 's/dynamic_cast<SwigDirector_/static_cast<SwigDirector_/g' libtorrent_jni.cpp
    else
	sed -i 's/dynamic_cast<SwigDirector_/static_cast<SwigDirector_/g' libtorrent_jni.cpp	
    fi
    
    # replace jlibtorrent version
    #uname=`uname -s`
    if [ "$(uname)" == "Darwin" ]; then
	sed -i '' 's/\$JLIBTORRENT_VERSION\$/'"${JLIBTORRENT_VERSION}"'/g' ../src/main/java/com/frostwire/jlibtorrent/swig/libtorrent_jni.java
    else
	sed -i 's/\$JLIBTORRENT_VERSION\$/'"${JLIBTORRENT_VERSION}"'/g' ../src/main/java/com/frostwire/jlibtorrent/swig/libtorrent_jni.java	
    fi
}

fixCode
runJni
refixCode
