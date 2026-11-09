# CMake toolchain for the C libraries WebTorrent needs that libtorrent builds with CMake from b2:
# usrsctp and libjuice (libtorrent/deps/libdatachannel/Jamfile runs `cmake ..` for them, with no
# toolchain options). build_libraries() in build-utils.shinc exports CMAKE_TOOLCHAIN_FILE pointing
# here (CMake >= 3.21 reads it from the environment), so they are built for the same OS and CPU as
# the rest of the native library, from the variables each build-<os>-<arch>.sh script exports:
# os_build, os_arch, CC, CXX, and for Android ANDROID_TOOLCHAIN and android_api.

set(_os "$ENV{os_build}")
set(_arch "$ENV{os_arch}")

if(_os STREQUAL "android")
    if(_arch STREQUAL "arm")
        set(ANDROID_ABI "armeabi-v7a")
    elseif(_arch STREQUAL "arm64")
        set(ANDROID_ABI "arm64-v8a")
    elseif(_arch STREQUAL "x86")
        set(ANDROID_ABI "x86")
    elseif(_arch STREQUAL "x86_64")
        set(ANDROID_ABI "x86_64")
    else()
        message(FATAL_ERROR "cmake-toolchain.cmake: unknown Android os_arch '${_arch}'")
    endif()
    set(ANDROID_PLATFORM "android-$ENV{android_api}")
    # ANDROID_TOOLCHAIN is <ndk>/toolchains/llvm/prebuilt/<host>
    get_filename_component(_ndk "$ENV{ANDROID_TOOLCHAIN}/../../../.." ABSOLUTE)
    include("${_ndk}/build/cmake/android.toolchain.cmake")
elseif(_os STREQUAL "windows")
    set(CMAKE_SYSTEM_NAME Windows)
    set(CMAKE_SYSTEM_PROCESSOR x86_64)
    set(CMAKE_C_COMPILER "$ENV{CC}")
    set(CMAKE_CXX_COMPILER "$ENV{CXX}")
    set(CMAKE_RC_COMPILER x86_64-w64-mingw32-windres)
elseif(_os STREQUAL "linux")
    set(CMAKE_SYSTEM_NAME Linux)
    if(_arch STREQUAL "arm64")
        set(CMAKE_SYSTEM_PROCESSOR aarch64)
    else()
        set(CMAKE_SYSTEM_PROCESSOR x86_64)
    endif()
    set(CMAKE_C_COMPILER "$ENV{CC}")
    set(CMAKE_CXX_COMPILER "$ENV{CXX}")
elseif(_os STREQUAL "macosx")
    # same minimum as config/macosx-*-config.jam, or the objects say macOS <SDK version>
    set(CMAKE_OSX_DEPLOYMENT_TARGET "11.3" CACHE STRING "")
    set(CMAKE_OSX_ARCHITECTURES "${_arch}" CACHE STRING "")
else()
    message(FATAL_ERROR "cmake-toolchain.cmake: unknown os_build '${_os}'")
endif()

# a cross compiler's own sysroot only: don't pick up the build machine's libraries
if(CMAKE_CROSSCOMPILING)
    set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
    set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
    set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
endif()
