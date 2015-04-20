@echo off
rem This script is mean to be executed from the root folder of this project as: `build\build_windows.bat`

rem =====================================================================================================================================================
rem DOWNLOAD BOOST BINARIES THAT MATCH YOUR VISUAL STUDIO VERSION
rem =====================================================================================================================================================
rem WARNING: Stay away from boost 1.56, 1.57, networking functions will cause a periodic freezing with libtorrent.
rem You will need Visual Studio 2012 for its cl.exe compiler.
rem Add the following folders to the %PATH% environment variable
rem C:\Program Files (x86)\Microsoft Visual Studio 11.0\VC\bin (or similar depending on what you have in your system)
rem C:\Program Files (x86)\Microsoft Visual Studio 11.0\Common7\IDE
rem 1. Download boost_1_55_0-msvc-11.0-32.exe [http://sourceforge.net/projects/boost/files/boost-binaries/1.55.0-build2/boost_1_55_0-msvc-11.0-32.exe/download]
rem 2. Extract it in "c:\boost_1_55_0"
rem 2. invoke bootstrap.bat to have bjam working, you will need it to build libtorrent
rem To build (1.58)
rem %BOOST_ROOT%\b2 cxxflags="%CXXFLAGS%" variant=release link=static runtime-link=static --stagedir=windows stage --without-context --without-coroutine --without-python --without-mpi --without-wave --without-test --without-graph --without-graph_parallel --without-iostreams

rem =====================================================================================================================================================
rem BUILDING LIBTORRENT
rem =====================================================================================================================================================
rem Download a libtorrent subversion "trunk/" snapshot from http://sourceforge.net/p/libtorrent/code/HEAD/tree/trunk/
rem set BOOST_ROOT=C:\boost_1_58_0
rem set CXXFLAGS=to the actual value (see below)
rem %BOOST_ROOT%\bjam toolset=msvc cxxflags="/I\"%BOOST_ROOT%\"" variant=release link=static runtime-link=static deprecated-functions=off logging=off

rem =====================================================================================================================================================
rem BUILD OPTIONS AND VARIABLES (adjust paths depending on your build environment)
rem =====================================================================================================================================================
echo on
set CXXFLAGS=/MT /O2 /fp:fast /nologo /DWIN32 /EHsc /c /GL /DBOOST_ASIO_DISABLE_CONNECTEX=1 /DNDEBUG=1 /DBOOST_ASIO_SEPARATE_COMPILATION=1 /DTORRENT_DISABLE_GEO_IP=1 /DTORRENT_NO_DEPRECATE=1 /DWIN32=1

set BOOST_ROOT=C:\boost_1_58_0
set LIBTORRENT_ROOT=C:\libtorrent-trunk
set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_25
set VS_VERSION=12
set VS_ROOT=C:\Program Files (x86)\Microsoft Visual Studio %VS_VERSION%.0

set VS_INCLUDE=/I"%VS_ROOT%\VC\include"
set BOOST_INCLUDE=/I"%BOOST_ROOT%"
set JAVA_INCLUDES=/I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" 
set LIBTORRENT_INCLUDE=/I"%LIBTORRENT_ROOT%\include" /I"%LIBTORRENT_ROOT%\include\libtorrent"

set INCLUDE_PATHS=%VS_INCLUDE% %BOOST_INCLUDE% %LIBTORRENT_INCLUDE% %JAVA_INCLUDES%

set BOOST_LIBPATH=%BOOST_ROOT%\windows\lib
set LIBTORRENT_LIBPATH=%LIBTORRENT_ROOT%\bin\msvc-%VS_VERSION%.0\release\deprecated-functions-off\link-static\logging-off\runtime-link-static\threading-multi
set VS_LIBPATH=%VS_ROOT%\VC\lib

@echo off
rem =====================================================================================================================================================
rem LINK OPTIONS AND VARIABLES 
rem =====================================================================================================================================================
rem (If you have cygwin/bin on your PATH, you must remove it from the PATH, 
rem otherwise link.exe will be GNU's link and it's not compatible with the options passed below.
echo on

set LIBS=advapi32.lib libtorrent.lib
set LOPT=/RELEASE /NOLOGO /MACHINE:X86 /ENTRY:_DllMainCRTStartup@12 /DLL /LTCG
set LIB_PATHS=/LIBPATH:%BOOST_LIBPATH% /LIBPATH:%LIBTORRENT_LIBPATH% /LIBPATH:"%VS_LIBPATH%"

cl %CXXFLAGS% %INCLUDE_PATHS% swig\libtorrent_jni.cpp
link %LOPT% %LIB_PATHS% /OUT:jlibtorrent.dll %LIBS% libtorrent_jni.obj
copy jlibtorrent.dll binaries\windows\x86\