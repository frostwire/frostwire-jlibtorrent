# How to build

If you are on a mac, this command will automatically pick the right architecture build scripts.

All build-steps:
```
./build-macos.sh 
```

If you are fixing something, perhaps you need to just issues the SWIG related steps after you've first run the whole thing which sets up all the dependencies (by building them, e.g. boost, openssl, swig, libtorrent) but then you don't want to waste time building everything.

If you're working on the wrapping side of things with SWIG scripting, you will want to do:

```
./build-macos.sh --swig-only
```

or if you're done with SWIG fixes, and now you're working on the Java Abstraction Layer, you don't need to re-wrap, you just need to build binaries and jars, so you now have this option, that will skip the SWIG step

```
./build-macos.sh --build-only
```


# Building SWIG from Source
You can get the copy we use to build SWIG and build it the same way the Dockerfile does it from frostwire.com:

```bash
export SWIG_VERSION=4.2.1
cd ~/src \ # change this to the path where you keep source codes in your system
wget -nv https://dl.frostwire.com/other/swig/swig-${SWIG_VERSION}.tar.gz
tar xzf swig-${SWIG_VERSION}.tar.gz
cd ~/src/swig-${SWIG_VERSION}
./configure --without-d
make -j 16
sudo make install
swig -version
```

# Binary debugging tips (sep-11 2021)

## More explicit native crashes
When you get a native crash log and the libtorrent function names are not explicit, if you can replicate the crash
update the `swig/config/android-<arch>-config.jam` file and set the `<cxxflag>-fvisibility=default` (instead of `hidden`),
rebuilt with symbol visibility on and and you will be able to get closer to the crash in C++ land, usually a `nullptr` dereference.

## Isolating native crashes in adb logcat using ndk-stack

Make sure your `android-ndk-r<version>` folder is in the `$PATH` and invoke `adb logcat` like this

`adb logcat | ndk-stack -sym <path-to-shared-library-folder>`

For example:

`adb logcat |  ndk-stack -sym ~/workspace.frostwire/frostwire-jlibtorrent/swig/bin/release/android/armeabi-v7a`

# About ifaddr.patch
This patch was created inside the Dockerfile /src/libtorrent folder.
Its purpose is to turn off netlink sockets in favor of iffaddr sockets which are now supported in android
The patch is meant to be applied inside the /src/libtorrent folder of the target image
The build script therefore builds first for windows and linux, and then applies the patch so that
the android libraries can be built. This patch might need to be revised if these files change in libtorrent
include/libtorrent/config.hpp
include/libtorrent/alert.hpp
src/enum_net.cpp

# building everything - March 14th 2021
1. On macos: `cd swig && ./build-macos.sh && ./docker_build_binaries.sh`

# building everything except macos libraries
2. On any OS: `cd swig && ./docker_build_binaries.sh`

# libtorrent.i, libtorrent.h

libtorrent.i contains SWIG extensions, ignores, and directives needed to wrap the libtorrent library.
It includes `libtorrent.h` where other C++ functions are defined manually.

# Notes on boost building issues

## ON BOOST ISSUES, DEC 13 2020

### Reverting to boost 1.73.
To build on macOS you need to comment the following line on `${BOOST_ROOT}/tools/build/src/tools/darwin.jam`
`flags darwin.compile.c++ OPTIONS $(condition) : -fcoalesce-templates ;`
We might have to do this in Travis CI

### 1.74 seems to require -frtti linking
```
In file included from /Users/gubatron/src/libtorrent/src/announce_entry.cpp:34:
In file included from /Users/gubatron/src/libtorrent/include/libtorrent/announce_entry.hpp:41:
In file included from /Users/gubatron/src/libtorrent/include/libtorrent/socket.hpp:53:
In file included from /Users/gubatron/src/boost_1_74_0/boost/asio/ip/tcp.hpp:19:
In file included from /Users/gubatron/src/boost_1_74_0/boost/asio/basic_socket_acceptor.hpp:19:
In file included from /Users/gubatron/src/boost_1_74_0/boost/asio/any_io_executor.hpp:22:
In file included from /Users/gubatron/src/boost_1_74_0/boost/asio/execution.hpp:19:
/Users/gubatron/src/boost_1_74_0/boost/asio/execution/any_executor.hpp:811:12: error: use of typeid requires -frtti
  return typeid(void);
```

### 1.75 causes swig to not generate error_code.java and other error handling classes

With boost 1.75 swig will issue these boost related warnings, something must have changed
```
Warning 315: Nothing known about 'boost::system::generic_category'
Warning 315: Nothing known about 'boost::system::system_category'
```
