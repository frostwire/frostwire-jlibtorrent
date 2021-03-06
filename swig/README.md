# building everything - Feb 7th
1. On macos: ./build-macos.sh
2. On jlibtorrent ec2 machine ubuntu: ./build_on_jlibtorrent_machine.sh (builds everything except windows x86)
3. On vmware ubuntu: ./build_from_vmware.sh (builds windows x86 only - not necessary for frostwire)
4. On vmware ubuntu: ./download_android_binaries_from_jlibtorrent_machine.sh

# libtorrent.i, libtorrent.h

libtorrent.i contains SWIG extensions, ignores, and directives needed to wrap the libtorrent library.
It includes `libtorrent.h` where other C++ functions are defined manually.

# run-swig.sh

Whenever you make changes to libtorrent.i and you need SWIG to re-write java classes in the parent java folders you must run this file manually.

It's not invoked by any of the build-scripts. The build-scripts assume the autogenerated java code by SWIG is already there.

# build-utils.shinc, build-macos.sh, build-linux-x86_64.sh, build-android-arm.sh, build-andriod-arm64.sh, build-android-x86.sh, build-android-x86_64.sh

These are scripts meant to be run on your local machine so that you can debug and test any changes you may have done.

Otherwise you'll have to push your local changes to your repository, which should be configured with Travis-CI, and AWS S3 to have travis remotely build your latest changes.

This is a very slow process, therefore we have these build scripts which are meant to be identical to ../travis.yml

# package-remote-build.sh

This script downloads the latest .so, .dlls and .dylib files from https://s3.amazonaws.com/gubatron-jlibtorrent/release/<os_build>/<os_arch>/<library>, places them inside the swig/bin/release/<os_build>/<os_arch> and then invokes the parent folder's `gradle build` command to generate all the release .jars we distribute at
https://github.com/frostwire/frostwire-jlibtorrent/releases

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