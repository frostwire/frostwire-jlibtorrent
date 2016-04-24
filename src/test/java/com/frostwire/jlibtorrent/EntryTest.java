package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.string_entry_map;
import com.frostwire.jlibtorrent.swig.string_vector;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author gubatron
 * @author aldenml
 */
public class EntryTest {

    @Test
    public void testSimpleCreation() {
        Map<String, Object> m = new HashMap<>();

        m.put("a", 1);
        m.put("b", "b");
        m.put("c", new Entry("es"));

        Entry e = Entry.fromMap(m);

        List<Object> l = new ArrayList<Object>();

        l.add("l1");
        l.add("l2");

        m.put("m", e);
        m.put("l", l);

        e = Entry.fromMap(m);
        assertNotNull(e);

        string_entry_map dict = e.swig().dict();
        string_vector keys = dict.keys();
        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i);
            assertNotNull(dict.get(k).to_string());
        }
    }

    @Test
    public void testCreation1() {
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
        final Map<String, Object> urlListMap = new HashMap<String, Object>();
        urlListMap.put("url-list", urlList);
        final String javaAPIBencodedMapString = new String(Entry.fromMap(urlListMap).bencode());

        final String expectedBencodedList = "d8:url-listl18:http://server1.com18:http://server2.comee";
        assertEquals(expectedBencodedList, oldSchoolBencodedMapString);
        assertEquals(expectedBencodedList, javaAPIBencodedMapString);

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
        licenseMap.put("license", Entry.fromMap(openSourceMap).swig());

        final String expectedLicenseBencoded = "d7:licensed11:open-sourced17:attributionAuthor13:FrostWire LLC16:attributionTitle15:FrostWire 5.7.714:attributionUrl24:http://www.frostwire.com10:licenseUrl37:https://www.gnu.org/licenses/gpl.htmleee";
        final String bencodedLicenseMap = new String(Entry.fromMap(licenseMap).bencode());
        assertEquals(expectedLicenseBencoded, bencodedLicenseMap);
    }
}
