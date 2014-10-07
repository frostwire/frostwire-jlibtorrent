set CXXFLAGS=/O2 /fp:fast /DTORRENT_USE_IPV6=1 /DNDEBUG=1 /I"C:\boost_1_56_0"

bjam toolset=msvc variant=release deprecated-functions=off link=static runtime-link=static

# for android
#export CXXFLAGS="-O3 -DTORRENT_USE_IPV6=1 -DNDEBUG=1 -I$BOOST_ROOT"
#$BOOST_ROOT/bjam toolset=gcc-androidarm variant=release deprecated-functions=off link=static runtime-link=static
