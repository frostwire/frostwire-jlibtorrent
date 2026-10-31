// Who frees the native object behind a proxy (frostwire/frostwire-jlibtorrent#337).
//
// Java-side only: these typemaps change the generated proxies, never libtorrent_jni.cpp.
// OwnedPointer.java (hand-written, next to the generated proxies) describes the three states:
//
// - Owned: swigCMemOwn is true and swigOwnedPtr is armed. The object is freed once, by delete() or
//   by the cleaner thread after the proxy becomes unreachable.
// - Borrowed: swigCMemOwn is false. The proxy points into memory another proxy owns, and keeps that
//   proxy reachable in swigBorrowedFrom.
// - Transferred: swigRelease() disarmed swigOwnedPtr and handed the object to C++.
//
// Base classes own their object only when they are the most derived class: a derived proxy passes
// cMemoryOwn=false to its base and adopts the pointer itself, with its own (static) deleter, since
// most libtorrent destructors aren't virtual.
//
// JLIBTORRENT_BORROW_OWNER and JLIBTORRENT_CTOR_BORROW are placeholders that run-swig.sh resolves
// after SWIG runs (resolve-borrow-owner.pl): Java typemaps can't tell a static method from an
// instance method, and `this` doesn't compile in a static one.
// - JLIBTORRENT_BORROW_OWNER becomes `this` in instance methods, `null` in static ones. Static
//   methods return memory no Java proxy owns: libtorrent's static and global constants, and
//   alert::cast_to_*() (alerts belong to the session). A new static function that returns a view
//   into one of its arguments needs its own typemap.
// - JLIBTORRENT_CTOR_BORROW becomes the constructor's first proxy argument (see create_torrent).

// ---------------------------------------------------------------------------------------------
// Proxy classes

%typemap(javabody) SWIGTYPE %{
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;
  // Armed while this proxy owns *swigCPtr (null when it doesn't, or a derived class does).
  private transient final OwnedPointer swigOwnedPtr;
  // The proxy whose native memory this one points into (borrowed views only).
  private transient Object swigBorrowedFrom;

  protected $javaclassname(long cPtr, boolean cMemoryOwn) {
    this(cPtr, cMemoryOwn, null);
  }

  protected $javaclassname(long cPtr, boolean cMemoryOwn, Object borrowedFrom) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
    swigBorrowedFrom = borrowedFrom;
    swigOwnedPtr = cMemoryOwn ? OwnedPointer.adopt(this, cPtr, $javaclassname::swigDeleteNative) : null;
  }

  protected static long getCPtr($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease($javaclassname obj) {
    long ptr = 0;
    if (obj != null) {
      synchronized (obj) {
        if (!obj.swigCMemOwn)
          throw new RuntimeException("Cannot release ownership as memory is not owned");
        ptr = obj.swigCPtr;
        obj.swigCMemOwn = false;
        obj.swigDisarm();
        obj.delete();
      }
    }
    return ptr;
  }

  // Ownership moves to C++: the cleaner must never free the object.
  protected void swigDisarm() {
    if (swigOwnedPtr != null)
      swigOwnedPtr.release();
  }
%}

%typemap(javabody_derived) SWIGTYPE %{
  private transient long swigCPtr;
  // Armed while this proxy owns *swigCPtr (null when it doesn't, or a derived class does).
  private transient final OwnedPointer swigOwnedPtr;

  protected $javaclassname(long cPtr, boolean cMemoryOwn) {
    this(cPtr, cMemoryOwn, null);
  }

  protected $javaclassname(long cPtr, boolean cMemoryOwn, Object borrowedFrom) {
    super($imclassname.$javaclazznameSWIGUpcast(cPtr), false, borrowedFrom);
    swigCPtr = cPtr;
    swigCMemOwn = cMemoryOwn;
    swigOwnedPtr = cMemoryOwn ? OwnedPointer.adopt(this, cPtr, $javaclassname::swigDeleteNative) : null;
  }

  protected static long getCPtr($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease($javaclassname obj) {
    long ptr = 0;
    if (obj != null) {
      synchronized (obj) {
        if (!obj.swigCMemOwn)
          throw new RuntimeException("Cannot release ownership as memory is not owned");
        ptr = obj.swigCPtr;
        obj.swigCMemOwn = false;
        obj.swigDisarm();
        obj.delete();
      }
    }
    return ptr;
  }

  @Override
  protected void swigDisarm() {
    if (swigOwnedPtr != null)
      swigOwnedPtr.release();
    super.swigDisarm();
  }
%}

// delete() frees the object now, if this proxy owns it, and is idempotent. swigDeleteNative is the
// static deleter the cleaner calls: it never sees the proxy.
%typemap(javadestruct, methodname="delete", methodmodifiers="public synchronized", parameters="") SWIGTYPE %{{
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        swigOwnedPtr.free();
      }
      swigCPtr = 0;
    }
    swigBorrowedFrom = null;
  }

  private static void swigDeleteNative(long swigCPtr) {
    $jnicall;
  }%}

%typemap(javadestruct_derived, methodname="delete", methodmodifiers="public synchronized", parameters="") SWIGTYPE %{{
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        swigOwnedPtr.free();
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  private static void swigDeleteNative(long swigCPtr) {
    $jnicall;
  }%}

// ---------------------------------------------------------------------------------------------
// Types freed only by an explicit delete(), as before #337:
// - directors: C++ keeps calling into them (alert notify callback, posix_wrapper, listeners), and
//   swig_plugin is handed to a shared_ptr by session_handle::add_extension
// - session: its destructor blocks until libtorrent shuts down; stop it explicitly
//   (SessionManager.stop()), never on the cleaner thread

%define JLIBTORRENT_EXPLICIT_DELETE(TYPE)
%typemap(javabody) TYPE %{
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;
  // The proxy whose native memory this one points into (borrowed views only).
  private transient Object swigBorrowedFrom;

  protected $javaclassname(long cPtr, boolean cMemoryOwn) {
    this(cPtr, cMemoryOwn, null);
  }

  protected $javaclassname(long cPtr, boolean cMemoryOwn, Object borrowedFrom) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
    swigBorrowedFrom = borrowedFrom;
  }

  protected static long getCPtr($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease($javaclassname obj) {
    long ptr = 0;
    if (obj != null) {
      synchronized (obj) {
        if (!obj.swigCMemOwn)
          throw new RuntimeException("Cannot release ownership as memory is not owned");
        ptr = obj.swigCPtr;
        obj.swigCMemOwn = false;
        obj.delete();
      }
    }
    return ptr;
  }

  // Never armed: only delete() frees this type.
  protected void swigDisarm() {
  }
%}

%typemap(javabody_derived) TYPE %{
  private transient long swigCPtr;

  protected $javaclassname(long cPtr, boolean cMemoryOwn) {
    this(cPtr, cMemoryOwn, null);
  }

  protected $javaclassname(long cPtr, boolean cMemoryOwn, Object borrowedFrom) {
    super($imclassname.$javaclazznameSWIGUpcast(cPtr), false, borrowedFrom);
    swigCPtr = cPtr;
    swigCMemOwn = cMemoryOwn;
  }

  protected static long getCPtr($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease($javaclassname obj) {
    long ptr = 0;
    if (obj != null) {
      synchronized (obj) {
        if (!obj.swigCMemOwn)
          throw new RuntimeException("Cannot release ownership as memory is not owned");
        ptr = obj.swigCPtr;
        obj.swigCMemOwn = false;
        obj.delete();
      }
    }
    return ptr;
  }
%}

%typemap(javadestruct, methodname="delete", methodmodifiers="public synchronized", parameters="") TYPE {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        $jnicall;
      }
      swigCPtr = 0;
    }
    swigBorrowedFrom = null;
  }

%typemap(javadestruct_derived, methodname="delete", methodmodifiers="public synchronized", parameters="") TYPE {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        $jnicall;
      }
      swigCPtr = 0;
    }
    super.delete();
  }
%enddef

JLIBTORRENT_EXPLICIT_DELETE(alert_notify_callback)
JLIBTORRENT_EXPLICIT_DELETE(add_files_listener)
JLIBTORRENT_EXPLICIT_DELETE(set_piece_hashes_listener)
JLIBTORRENT_EXPLICIT_DELETE(swig_plugin)
JLIBTORRENT_EXPLICIT_DELETE(posix_wrapper)
JLIBTORRENT_EXPLICIT_DELETE(libtorrent::session)

// ---------------------------------------------------------------------------------------------
// Type wrappers (SWIGTYPE_p_...): opaque pointers, never freed, but they can point into a proxy.

%typemap(javabody) SWIGTYPE *, SWIGTYPE &, SWIGTYPE &&, SWIGTYPE [] %{
  private transient long swigCPtr;
  // The proxy whose native memory this one points into, kept reachable while this one is.
  @SuppressWarnings("unused")
  private transient Object swigBorrowedFrom;

  protected $javaclassname(long cPtr, @SuppressWarnings("unused") boolean futureUse) {
    this(cPtr, futureUse, null);
  }

  protected $javaclassname(long cPtr, @SuppressWarnings("unused") boolean futureUse, Object borrowedFrom) {
    swigCPtr = cPtr;
    swigBorrowedFrom = borrowedFrom;
  }

  protected $javaclassname() {
    swigCPtr = 0;
  }

  protected static long getCPtr($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }
%}

// ---------------------------------------------------------------------------------------------
// Borrowed returns: a reference or pointer into the object a method was called on keeps it
// reachable. $owner is true for %newobject results, which are owned instead.

%typemap(javaout) SWIGTYPE & {
    return new $javaclassname($jnicall, $owner, JLIBTORRENT_BORROW_OWNER);
  }
%typemap(javaout) SWIGTYPE && {
    return new $javaclassname($jnicall, $owner, JLIBTORRENT_BORROW_OWNER);
  }
%typemap(javaout) SWIGTYPE *, SWIGTYPE [] {
    long cPtr = $jnicall;
    return (cPtr == 0) ? null : new $javaclassname(cPtr, $owner, JLIBTORRENT_BORROW_OWNER);
  }
%typemap(javaout) SWIGTYPE *const& {
    long cPtr = $jnicall;
    return (cPtr == 0) ? null : new $*javaclassname(cPtr, $owner, JLIBTORRENT_BORROW_OWNER);
  }

// Values that are views: the proxy owns the small view object, which points into the object the
// method was called on (a bdecode_node into its root's tokens and buffer, a span or string_view
// into its storage, a map iterator into its map).
%define JLIBTORRENT_VIEW_VALUE(TYPE...)
%typemap(javaout) TYPE {
    return new $&javaclassname($jnicall, true, JLIBTORRENT_BORROW_OWNER);
  }
%enddef

JLIBTORRENT_VIEW_VALUE(libtorrent::bdecode_node)
JLIBTORRENT_VIEW_VALUE(libtorrent::span<char>)
JLIBTORRENT_VIEW_VALUE(libtorrent::span<char const>)
JLIBTORRENT_VIEW_VALUE(libtorrent::span<std::int8_t const>)
JLIBTORRENT_VIEW_VALUE(libtorrent::string_view)
JLIBTORRENT_VIEW_VALUE(std::pair<libtorrent::bdecode_node, libtorrent::bdecode_node>)
JLIBTORRENT_VIEW_VALUE(std::pair<libtorrent::string_view, libtorrent::bdecode_node>)

// A map iterator borrows from its map. The next one borrows from the same map, not from the
// iterator it came from, so walking a map doesn't chain every iterator to the previous one.
%define JLIBTORRENT_MAP_ITERATOR(TYPE...)
JLIBTORRENT_VIEW_VALUE(TYPE)
%typemap(javaout) TYPE getNextUnchecked {
    return new $&javaclassname($jnicall, true, swigBorrowedFrom);
  }
%enddef

JLIBTORRENT_MAP_ITERATOR(std::map<int, std::string>::iterator)
JLIBTORRENT_MAP_ITERATOR(std::map<std::string, std::string>::iterator)
JLIBTORRENT_MAP_ITERATOR(std::map<int, libtorrent::bitfield>::iterator)
JLIBTORRENT_MAP_ITERATOR(std::map<std::string, libtorrent::entry>::iterator)
JLIBTORRENT_MAP_ITERATOR(std::map<libtorrent::file_index_t, std::string>::iterator)
JLIBTORRENT_MAP_ITERATOR(std::map<std::string, long>::iterator)
