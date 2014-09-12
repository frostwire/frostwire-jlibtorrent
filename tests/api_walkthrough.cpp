#include <iostream>
#include <boost/system/error_code.hpp>
#include <boost/filesystem/operations.hpp>
#include <boost/filesystem/path.hpp>
#include <boost/filesystem/fstream.hpp>
#include <boost/bind.hpp>
#include <libtorrent/bencode.hpp>
#include <libtorrent/create_torrent.hpp>
#include <libtorrent/file.hpp>
#include <libtorrent/entry.hpp>
#include <libtorrent/session.hpp>
#include <libtorrent/session_settings.hpp>
#include <libtorrent/torrent_handle.hpp>
#include <libtorrent/magnet_uri.hpp>
using namespace std;
using namespace boost::filesystem;
using namespace libtorrent;

bool add_file_filter(std::string const& f)
{
    if (filename(f)[0] == '.') return false;
    fprintf(stderr, "%s\n", f.c_str());
    return true;
}

void print_entry(entry bencoded_torrent_entry) {
    // create the torrent and print it to stdout
    std::vector<char> torrent_char_vector;
    bencode(back_inserter(torrent_char_vector), bencoded_torrent_entry);
    fwrite(&torrent_char_vector[0], 1, torrent_char_vector.size(), stdout);
    fclose(stdout);
}

void download_torrent_from_magnet(string magnet_url, entry& result) {
  session* s = new session(fingerprint("FW", 0, 1, 0, 0), std::make_pair(48900,49000),"0.0.0.0",0);
  session_settings sett;
  sett.user_agent = "fw";
  sett.tracker_receive_timeout = 1234;
  sett.file_pool_size = 543;
  sett.urlseed_wait_retry = 74;
  sett.file_pool_size = 754;
  sett.initial_picker_threshold = 351;
  sett.upnp_ignore_nonrouters = 5326;
  sett.coalesce_writes = 623;
  sett.auto_scrape_interval = 753;
  sett.close_redundant_connections = 245;
  sett.auto_scrape_interval = 235;
  sett.auto_scrape_min_interval = 62;
  s->set_settings(sett);

  dht_settings dht_sett;
  dht_sett.max_peers_reply=70;
  s->set_dht_settings(dht_sett);


  add_torrent_params p;
  libtorrent::error_code ec;
  p.save_path = ".";

  parse_magnet_uri(magnet_url,p,ec);

  torrent_handle th = s->add_torrent(p, ec);

  if (ec) fprintf(stderr, "%s\n", ec.message().c_str());

  s->add_torrent(p, ec);

}

void entry_to_lazy_entry(entry bencoded_entry, lazy_entry& result_lazy_entry) {
    // convert using dynamic char vector.
    std::vector<char> tmp;
    bencode(std::back_inserter(tmp), bencoded_entry);
    boost::system::error_code ec;
    lazy_bdecode(&tmp[0], &tmp[0] + tmp.size(), result_lazy_entry, ec);

    //or with a fixed size char array (might be too little memory)
    /**
    char tmp[1500];
    int size = bencode(tmp, bencoded_entry);
    boost::system::error_code ec;
    lazy_bdecode(tmp, tmp + size, result_lazy_entry, ec);
    */

    std::cout << "Entry to Lazy Entry: " << print_entry(result_lazy_entry, false, 4) << "\n";
}

int main() {
    int flags = create_torrent::optimize |
                         create_torrent::merkle |
                         create_torrent::calculate_file_hashes;
    int pad_file_limit = -1;
    int piece_size = 0;
    file_storage fs;

    create_torrent t(fs,piece_size,pad_file_limit,flags);

    std::string content_file_path = libtorrent::complete("/Users/gubatron/instassist.log");

    //the api is full of C-style useful calls like this, directly at the libtorrent:: namespace.
    add_files(fs, content_file_path, add_file_filter, flags);

    std::string tracker = "udp://tracker.frostwire.com:6969/announce";
    t.add_tracker(tracker,0);

    cout << "num pieces?    : " << t.num_pieces() << "\n";

    //This is the object that we'd manipulate to add custom info entries.
    entry myTorrentBencodedEntry = t.generate();

    //and then we can manipulate them, add our own keys.

    //add a string
    myTorrentBencodedEntry["info"]["foo-string"] = "bar";

    //add a (license) dictionary
    entry::dictionary_type licenseDict;
    licenseDict["url"] ="http://creativecommons.org/licenses/by-nc/3.0/";
    licenseDict["author"] = "Kellee Maize";
    entry licenseEntry(licenseDict);

    myTorrentBencodedEntry["info"]["license"] = licenseEntry;


    lazy_entry myTorrentLazyBencodedEntry;
    entry_to_lazy_entry(myTorrentBencodedEntry, myTorrentLazyBencodedEntry);

    //Not sure why these sometimes work, sometimes don't.


    if (myTorrentLazyBencodedEntry.dict_find("announce") != NULL) {
        std::cout << "announce: " << myTorrentLazyBencodedEntry.dict_find("announce")->string_cstr() << std::endl;
    }

    if (myTorrentLazyBencodedEntry.dict_find("creation date") != NULL) {
        std::cout << "creation date: " << myTorrentLazyBencodedEntry.dict_find("creation date")->int_value() << std::endl;
    }

    if (myTorrentLazyBencodedEntry.dict_find("info") != NULL) {
        lazy_entry* infoLazyEntry = myTorrentLazyBencodedEntry.dict_find("info");
        std::cout << "info! : " << print_entry(*infoLazyEntry, false, 4) << std::endl;
    }

    string magnet_url = "magnet:?xt=urn:btih:31e57ca5b87c5df6cbb764830b66c0b6417daeb3&dn=frostwire-5.7.6.windows.coc.premium.exe&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&iipp=0af0769099c2";
    entry magnet_entry;
    download_torrent_from_magnet(magnet_url, magnet_entry);

    return 0;
};


//NOTES:

// 1. Whenever possible, ``lazy_bdecode()`` should be preferred over ``bdecode()``.
// It is more efficient and more secure. It supports having constraints on the
// amount of memory is consumed by the parser.
//
// *lazy* refers to the fact that it doesn't copy any actual data out of the
// bencoded buffer.

// 2. torrent_info::parse_info_section (Relevant to searching through magnet links/info objects)
// if we manage to perform a 'info' lookup directly from another peer
// and we ever need a full blown torrent_info object, this is what we'd use.
//
// bool torrent_info::parse_info_section(lazy_entry const& e, error_code& ec, int flags);
//
// populates the torrent_info by providing just the info-dict buffer. This is used when
// loading a torrent from a magnet link for instance, where we only have the info-dict.
// The lazy_entry ``e`` points to a parsed info-dictionary. ``ec`` returns an error code
// if something fails (typically if the info dictionary is malformed). ``flags`` are currently
// unused.

// 3. android note: session_settings min_memory_usage();
// It lowers the max number of peers in the peer list for torrents. It performs multiple smaller reads when it hashes pieces, instead of reading it all into memory before hashing.
// This configuration is inteded to be the starting point for embedded devices. It will significantly reduce memory usage.
