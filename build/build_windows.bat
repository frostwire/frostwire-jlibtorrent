rem Review this
rem set CXXFLAGS=/O2 /fp:fast /DTORRENT_USE_IPV6=1 /DNDEBUG=1 /I"C:\boost_1_56_0"
rem bjam toolset=msvc variant=release deprecated-functions=off link=static runtime-link=static


set LIBS=advapi32.lib
set LOPT=/RELEASE /NOLOGO /MACHINE:X86 /ENTRY:_DllMainCRTStartup@12 /DLL
set CFLAGS=/O2 /fp:fast /nologo /DWIN32 /EHsc /c /DBOOST_ASIO_SEPARATE_COMPILATION=1 /DTORRENT_USE_IPV6=1 /DNDEBUG=1 /DTORRENT_NO_DEPRECATE=1 /DTORRENT_DISABLE_GEO_IP=1
set JAVA_INCLUDE=/I"C:\Program Files (x86)\Java\jdk1.8.0_20\include" /I"C:\Program Files (x86)\Java\jdk1.8.0_20\include\win32"
set BOOST_INCLUDE=/I"C:\boost_1_56_0"
set LIBTORRENT_INCLUDE=/I"C:\libtorrent-rasterbar-1.0.2\include"

cl %CFLAGS% %JAVA_INCLUDE% %BOOST_INCLUDE% %LIBTORRENT_INCLUDE% swig\libtorrent_jni.cpp
link %LOPT% /OUT:jlibtorrent.dll %LIBS% libtorrent.lib  libtorrent_jni.obj
