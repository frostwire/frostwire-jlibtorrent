%BOOST_ROOT%\bjam toolset=msvc
set TARGET=bin\msvc-12.0\release\boost-source\crypto-openssl\deprecated-functions-off\threading-multi\jlibtorrent.dll
copy %TARGET% ..\binaries\windows\x86\
copy %TARGET% ..\