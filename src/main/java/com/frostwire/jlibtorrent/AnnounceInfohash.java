package com.frostwire.jlibtorrent;
import com.frostwire.jlibtorrent.swig.announce_infohash;

public class AnnounceInfohash {
    private final announce_infohash swig;

    public AnnounceInfohash(announce_infohash infohash) {
        swig = infohash;
    }

    /**
     * If this tracker has returned an error or warning message
     * that message is stored here.
     *
     * @return the error or warning message
     */
    public String message() {
        return swig.getMessage();
    }

    /**
     * The number of times in a row we have failed to announce to this
     * tracker.
     *
     * @return number of announce fails
     */
    public short fails() {
        return swig.getFails();
    }

    /**
     * Returns true while we're waiting for a response from the tracker.
     *
     * @return true if waiting
     */
    public boolean updating() {
        return swig.getUpdating();
    }

    /**
     * Returns true if the last time we tried to announce to this
     * tracker succeeded, or if we haven't tried yet.
     */
    public boolean isWorking() {
        return swig.getFails() == 0;
    }
}
