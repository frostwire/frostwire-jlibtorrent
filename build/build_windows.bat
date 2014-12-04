rem set CXXFLAGS=/MT /O2 /fp:fast /DNDEBUG=1 /I"C:\boost_1_55_0"
rem bjam toolset=msvc variant=release link=static runtime-link=static deprecated-functions=off


set LIBS=advapi32.lib
set LOPT=/RELEASE /NOLOGO /MACHINE:X86 /ENTRY:_DllMainCRTStartup@12 /DLL
set CFLAGS=/MT /O2 /fp:fast /nologo /DWIN32 /EHsc /c /DNDEBUG=1 /DBOOST_ASIO_SEPARATE_COMPILATION=1 /DTORRENT_DISABLE_GEO_IP=1 /DTORRENT_NO_DEPRECATE=1
set JAVA_INCLUDE=/I"C:\Program Files (x86)\Java\jdk1.8.0_25\include" /I"C:\Program Files (x86)\Java\jdk1.8.0_25\include\win32"
set BOOST_INCLUDE=/I"C:\boost_1_55_0"
set LIBTORRENT_INCLUDE=/I"C:\libtorrent-rasterbar-1.0.2\include"

cl %CFLAGS% %JAVA_INCLUDE% %BOOST_INCLUDE% %LIBTORRENT_INCLUDE% swig\libtorrent_jni.cpp
link %LOPT% /OUT:jlibtorrent.dll %LIBS% libtorrent.lib  libtorrent_jni.obj
