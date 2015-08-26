%BOOST_ROOT%\bjam toolset=msvc include="%OPENSSL_ROOT%\\include" location=bin/windows/x86
set TARGET=bin\windows\x86\jlibtorrent.dll
copy %TARGET% ..\