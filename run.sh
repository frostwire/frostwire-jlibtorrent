#!/bin/bash

if [ $# -eq 0 ]; then
    echo "usage: run.sh <toolname> args"
    exit 1
fi

TOOL_NAME=`echo $1 | awk '{ print tolower($1) }'`
shift
TOOL_ARGS=$@

TOOL_CLASS=""

case "$TOOL_NAME" in
  "announce") TOOL_CLASS="Announce";;
  "getpeers") TOOL_CLASS="GetPeers";;
  "ltversion") TOOL_CLASS="LTVersion";;
  "mktorrent") TOOL_CLASS="MkTorrent";;
  "readtorrent") TOOL_CLASS="ReadTorrent";;
  * ) echo "Tool name not supported"; exit 1;;
esac

java \
    -cp repo/com/frostwire/jlibtorrent/1.0/jlibtorrent-1.0.jar \
    "com.frostwire.jlibtorrent.tools.$TOOL_CLASS" ${TOOL_ARGS}
