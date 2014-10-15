package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public final class EntryTest2 {

    public static void main(String[] args) throws Throwable {
        //old school using libtorrent's (tedious if i may say) entry api
        final entry url_list = new entry();
        url_list.list().push_back(new entry("http://server1.com"));
        url_list.list().push_back(new entry("http://server2.com"));
        final entry swig_entry = new entry();
        swig_entry.dict().set("url-list", url_list);
        final Entry e = new Entry(swig_entry);
        final String oldSchoolBencodedMapString = new String(e.bencode());

        //now using Java's collection API
        final List<String> urlList = new ArrayList<String>();
        urlList.add("http://server1.com");
        urlList.add("http://server2.com");
        final Map<String,Object> urlListMap = new HashMap<String, Object>();
        urlListMap.put("url-list", urlList);
        final String javaAPIBencodedMapString = new String(Entry.fromMap(urlListMap).bencode());

        final String expectedBencodedList = "d8:url-listl18:http://server1.com18:http://server2.comee";
        assert(expectedBencodedList.equals(oldSchoolBencodedMapString));
        assert(expectedBencodedList.equals(javaAPIBencodedMapString));
        System.out.println("expected          : " + expectedBencodedList);
        System.out.println("actual old school : " + oldSchoolBencodedMapString);
        System.out.println("actual java api   : " + javaAPIBencodedMapString);

        final Map<String, entry> torrentMap = new HashMap<String, entry>();
        torrentMap.put("Comment", new entry("Torrent created with FrostWire"));

        final Map<String, Object> ccMap = new HashMap<String, Object>();
        ccMap.put("attributionAuthor", new entry("FrostWire LLC"));
        ccMap.put("attributionTitle", new entry("FrostWire 5.7.7"));
        ccMap.put("attributionUrl", "http://www.frostwire.com"); //on purpose not an entry
        ccMap.put("licenseUrl", new entry("https://www.gnu.org/licenses/gpl.html"));

        final Map<String, Object> openSourceMap = new HashMap<String, Object>();
        //works both with an entry object created out of a Map, or straight up with the Map.
        //openSourceMap.put("open-source", Entry.fromMap(ccMap).getSwig());
        openSourceMap.put("open-source", ccMap);

        final Map<String, entry> licenseMap = new HashMap<String, entry>();
        licenseMap.put("license", Entry.fromMap(openSourceMap).getSwig());

        final String expectedLicenseBencoded = "d7:licensed11:open-sourced17:attributionAuthor13:FrostWire LLC16:attributionTitle15:FrostWire 5.7.714:attributionUrl24:http://www.frostwire.com10:licenseUrl37:https://www.gnu.org/licenses/gpl.htmleee";
        final String bencodedLicenseMap = new String(Entry.fromMap(licenseMap).bencode());
        assert(expectedLicenseBencoded.equals(bencodedLicenseMap));

        System.out.println("expected: " + expectedLicenseBencoded);
        System.out.println("computed: " + bencodedLicenseMap);
        System.out.println();
    }
}
