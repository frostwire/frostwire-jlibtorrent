{
  "targets": [
    {
      "target_name": "jlibtorrent",
      "sources": [ "libtorrent_node.cpp" ],
      "include_dirs": [
        "$(BOOST_ROOT)",
        "$(LIBTORRENT_ROOT)/include",
      ],
      'link_settings': {
        'libraries': [
          '-ltorrent',
          '-lboost_chrono',
          '-lboost_random',
          '-lboost_system',
        ],
      },
      'defines': [
        'BOOST_ALL_NO_LIB',
        'BOOST_ASIO_ENABLE_CANCELIO',
        'BOOST_ASIO_HASH_MAP_BUCKETS=1021',
        'BOOST_CHRONO_STATIC_LINK=1',
        'BOOST_EXCEPTION_DISABLE',
        'BOOST_MULTI_INDEX_DISABLE_SERIALIZATION',
        'BOOST_SYSTEM_NO_DEPRECATED',
        'BOOST_SYSTEM_STATIC_LINK=1',
        'NDEBUG',
        'TORRENT_DISK_STATS',
        'TORRENT_NO_DEPRECATE',
        'TORRENT_USE_I2P=1',
        'TORRENT_USE_OPENSSL',
        'TORRENT_UTP_LOG_ENABLE',
        'UNICODE',
        '_FILE_OFFSET_BITS=64',
        '_UNICODE',
        '_WIN32_WINNT=0x0501',
      ],
      'cflags!': [ '-fno-exceptions' ],
      'cflags_cc!': [ '-fno-exceptions' ],
      'conditions': [
        [ 'OS=="mac"', {
            'library_dirs': [
              '../bin/macosx/x86_64/',
            ],
            'xcode_settings': {
              'OTHER_CPLUSPLUSFLAGS' : ['-std=c++11','-stdlib=libc++'],
              'OTHER_LDFLAGS': ['-stdlib=libc++'],
              'GCC_ENABLE_CPP_EXCEPTIONS': 'YES',
              'MACOSX_DEPLOYMENT_TARGET': '10.9',
            },
          }
        ],
      ]
    }
  ]
}
