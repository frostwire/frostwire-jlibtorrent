package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;

/**
 * @author gubatron
 * @author aldenml
 */
public final class BDecodeNode {

    private final bdecode_node n;
    private final byte_vector buffer;

    /**
     * @param n the native object
     */
    public BDecodeNode(bdecode_node n) {
        this(n, null);
    }

    /**
     * Used to keep the buffer reference around.
     *
     * @param n      the native object
     * @param buffer the buffer backing up the native object
     */
    public BDecodeNode(bdecode_node n, byte_vector buffer) {
        this.n = n;
        this.buffer = buffer;
    }

    /**
     * @return the native object
     */
    public bdecode_node swig() {
        return n;
    }

    /**
     * This methods returns the internal buffer or null
     * if it was constructed without one.
     * <p>
     * This also prevent premature garbage collection in case
     * the node was created from a constructed buffer.
     *
     * @return the pinned buffer
     */
    public byte_vector buffer() {
        return buffer;
    }

    /**
     * A JSON style string representation
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return bdecode_node.to_string(n, false, 2);
    }

    public boolean hasList(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return false;
        }
        bdecode_node target_node = n.dict_find_list_s(key);
        return target_node.type() != bdecode_node.type_t.none_t;
    }

    public boolean hasDict(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return false;
        }
        bdecode_node target_node = n.dict_find_dict_s(key);
        return target_node.type() != bdecode_node.type_t.none_t;
    }

    public boolean hasString(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return false;
        }
        return n.dict_find_string_value_s(key) != null;
    }

    public boolean hasInt(String key) {
        if (n.type() != bdecode_node.type_t.int_t) {
            return false;
        }
        return n.dict_find_int_value_s(key,0) != 0;
    }

    public BDecodeNode getList(String key) {
        if (!hasList(key)) {
            return null;
        }

        BDecodeNode result = new BDecodeNode(n.dict_find_list_s(key));
        return result;
    }

    public BDecodeNode getDict(String key) {
        if (!hasDict(key)) {
            return null;
        }

        BDecodeNode result = new BDecodeNode(n.dict_find_dict_s(key));
        return result;
    }

    public String getString(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return null;
        }
        return n.dict_find_string_value_s(key);
    }

    public long getInt(String key) {
        if (n.type() != bdecode_node.type_t.int_t) {
            return 0;
        }
        return n.dict_find_int_value_s(key,0);
    }

    public static BDecodeNode bdecode(byte[] data) {
        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node n = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, n, ec);

        if (ret == 0) {
            return new BDecodeNode(n, buffer);
        } else {
            throw new IllegalArgumentException("Can't decode data: " + ec.message());
        }
    }
}
