@echo off
rem =====================================================================================================================================================
rem BUILDING BOOST
rem =====================================================================================================================================================
rem You will need Visual Studio 2012 for its cl.exe compiler, and the Windows 8 SDK to have all the necessary header files. 
rem Add the following folders to the %PATH% environment variable
rem C:\Program Files (x86)\Microsoft Visual Studio 11.0\VC\bin (or similar depending on what you have in your system)
rem C:\Program Files (x86)\Microsoft Visual Studio 11.0\Common7\IDE
rem 1. download boost_1_55_0, put it in c:\boost_1_55_0 (do not use 1_56 as it creates a 100% CPU issue, 1_57 hasn't been tested)
rem 2. invoke bootstrap.bat
rem 3. b2

rem =====================================================================================================================================================
rem BUILDING LIBTORRENT
rem =====================================================================================================================================================
rem set BOOST_ROOT=C:\boost_1_55_0
rem set CXXFLAGS=/MT /O2 /fp:fast /DNDEBUG=1 /I"C:\boost_1_55_0"
rem %BOOST_ROOT%\bjam toolset=msvc variant=release link=static runtime-link=static deprecated-functions=off

rem =====================================================================================================================================================
rem BUILD OPTIONS AND VARIABLES
rem =====================================================================================================================================================
echo on
set CFLAGS=/MT /O2 /fp:fast /nologo /DWIN32 /EHsc /c /DNDEBUG=1 /DBOOST_ASIO_SEPARATE_COMPILATION=1 /DTORRENT_DISABLE_GEO_IP=1 /DTORRENT_NO_DEPRECATE=1

set LIBTORRENT_ROOT=C:\libtorrent-code-10886-trunk
set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_25
set VS_ROOT=C:\Program Files (x86)\Microsoft Visual Studio 11.0

set VS_INCLUDE=/I"%VS_ROOT%\VC\include"
set BOOST_INCLUDE=/I"C:\boost_1_55_0"
set WIN8_SDK_INCLUDES=/I"C:\Program Files (x86)\Windows Kits\8.0\Include\um" /I"C:\Program Files (x86)\Windows Kits\8.0\Include\shared"
set JAVA_INCLUDES=/I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" 
set LIBTORRENT_INCLUDE=/I"%LIBTORRENT_ROOT%\include"

set INCLUDE_PATHS=%VS_INCLUDE% %BOOST_INCLUDE% %LIBTORRENT_INCLUDE% %WIN8_SDK_INCLUDES% %JAVA_INCLUDES%
set LIBTORRENT_LIBPATH=%LIBTORRENT_ROOT%\bin\msvc-11.0\release\deprecated-functions-off\link-static\runtime-link-static\threading-multi

rem =====================================================================================================================================================
rem LINK OPTIONS AND VARIABLES 
rem =====================================================================================================================================================
rem (If you have cygwin/bin on your PATH, you must remove it from the PATH, 
rem otherwise link.exe will be GNU's link and it's not compatible with the options passed below.

set LIBS=advapi32.lib libtorrent.lib
set LOPT=/RELEASE /NOLOGO /MACHINE:X86 /ENTRY:_DllMainCRTStartup@12 /DLL
set LIB_PATHS=/LIBPATH:%LIBTORRENT_LIBPATH% /LIBPATH:"%VS_ROOT%\VC\lib" /LIBPATH:%BOOST_INCLUDE%\bin.v2\libs\system\build\msvc-11.0\release\link-static\threading-multi

cl %CFLAGS% %INCLUDE_PATHS% swig\libtorrent_jni.cpp
link %LOPT% %LIB_PATHS% /OUT:jlibtorrent.dll %LIBSS% libtorrent_jni.obj
