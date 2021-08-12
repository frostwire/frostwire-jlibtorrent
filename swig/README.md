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
