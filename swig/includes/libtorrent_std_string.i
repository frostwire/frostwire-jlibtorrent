// std::string <-> java.lang.String through byte[] and real UTF-8 (frostwire/frostwire-jlibtorrent#336).
//
// SWIG's stock Java typemaps (std_string.i) use NewStringUTF / GetStringUTFChars, which speak JNI's
// *modified* UTF-8:
//  - C++ -> Java: bytes that aren't modified UTF-8 are undefined behavior. Android's CheckJNI aborts
//    the process; HotSpot garbles them (a 32-byte binary dictionary key came back as ""). Embedded
//    NULs truncate the string.
//  - Java -> C++: characters outside the BMP (emoji, many CJK extensions) arrive as CESU-8
//    surrogate pairs instead of UTF-8, and U+0000 as C0 80.
// libtorrent strings carry bytes chosen by other parties (bencoded data, peers, trackers), so the
// bytes cross as byte[] and Java does the UTF-8 work: new String(bytes, UTF_8) replaces invalid
// sequences with U+FFFD and keeps NULs; String.getBytes(UTF_8) produces real UTF-8. The public Java
// API keeps its String signatures. Include after <std_string.i>; these typemaps replace its ones.

%typemap(jni) std::string, const std::string & "jbyteArray"
%typemap(jtype) std::string, const std::string & "byte[]"
%typemap(jstype) std::string, const std::string & "String"

%typemap(javain) std::string, const std::string &
    "($javainput == null ? null : $javainput.getBytes(java.nio.charset.StandardCharsets.UTF_8))"

%typemap(javaout) std::string, const std::string & {
    byte[] bytes = $jnicall;
    return bytes == null ? null : new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
  }

%typemap(in) std::string %{
  if (!$input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return $null;
  }
  {
    jsize $1_len = jenv->GetArrayLength($input);
    jbyte* $1_bytes = jenv->GetByteArrayElements($input, nullptr);
    if (!$1_bytes) return $null;
    $1.assign(reinterpret_cast<char const*>($1_bytes), static_cast<std::size_t>($1_len));
    jenv->ReleaseByteArrayElements($input, $1_bytes, JNI_ABORT);
  }
%}

%typemap(in) const std::string & %{
  if (!$input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return $null;
  }
  std::string $1_str;
  {
    jsize $1_len = jenv->GetArrayLength($input);
    jbyte* $1_bytes = jenv->GetByteArrayElements($input, nullptr);
    if (!$1_bytes) return $null;
    $1_str.assign(reinterpret_cast<char const*>($1_bytes), static_cast<std::size_t>($1_len));
    jenv->ReleaseByteArrayElements($input, $1_bytes, JNI_ABORT);
  }
  $1 = &$1_str;
%}

%typemap(out) std::string %{
  $result = jenv->NewByteArray(static_cast<jsize>($1.size()));
  if ($result) {
    jenv->SetByteArrayRegion($result, 0, static_cast<jsize>($1.size()), reinterpret_cast<jbyte const*>($1.data()));
  }
%}

%typemap(out) const std::string & %{
  $result = jenv->NewByteArray(static_cast<jsize>($1->size()));
  if ($result) {
    jenv->SetByteArrayRegion($result, 0, static_cast<jsize>($1->size()), reinterpret_cast<jbyte const*>($1->data()));
  }
%}

// Directors: C++ calling a Java override with a std::string argument (add_files_listener.pred).
// The descriptor describes the Java override's parameter (String), which SWIG uses to detect that a
// subclass overrides the method; the bytes still cross as byte[] and javadirectorin decodes them.
%typemap(directorin, descriptor="Ljava/lang/String;") std::string, const std::string & %{
  $input = jenv->NewByteArray(static_cast<jsize>($1.size()));
  if ($input) {
    jenv->SetByteArrayRegion($input, 0, static_cast<jsize>($1.size()), reinterpret_cast<jbyte const*>($1.data()));
  }
  Swig::LocalRefGuard $1_refguard(jenv, $input);
%}

%typemap(javadirectorin) std::string, const std::string &
    "($jniinput == null ? null : new String($jniinput, java.nio.charset.StandardCharsets.UTF_8))"

// No director in jlibtorrent returns a std::string to C++; fail the build if one appears instead of
// silently falling back to modified UTF-8.
%typemap(directorout) std::string, const std::string & %{
#error "jlibtorrent: add a byte[] directorout typemap for std::string before returning one from a Java override"
%}
%typemap(javadirectorout) std::string, const std::string & "$javacall"

%typemap(typecheck) std::string, const std::string & = char *;

%typemap(throws) std::string, const std::string & %{
  SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, jlibtorrent_jni_safe_message($1).c_str());
  return $null;
%}

// The same for C strings: alert texts (file paths, peer/DHT/port-mapping log lines, tracker
// messages), version strings, and the posix_wrapper director's file paths. A null pointer maps to
// a null String and back.
%typemap(jni) char *, char const * "jbyteArray"
%typemap(jtype) char *, char const * "byte[]"
%typemap(jstype) char *, char const * "String"

%typemap(javain) char *, char const *
    "($javainput == null ? null : $javainput.getBytes(java.nio.charset.StandardCharsets.UTF_8))"

%typemap(javaout) char *, char const * {
    byte[] bytes = $jnicall;
    return bytes == null ? null : new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
  }

%typemap(in) char *, char const * (std::string temp) %{
  $1 = nullptr;
  if ($input) {
    jsize $1_len = jenv->GetArrayLength($input);
    jbyte* $1_bytes = jenv->GetByteArrayElements($input, nullptr);
    if (!$1_bytes) return $null;
    temp.assign(reinterpret_cast<char const*>($1_bytes), static_cast<std::size_t>($1_len));
    jenv->ReleaseByteArrayElements($input, $1_bytes, JNI_ABORT);
    $1 = const_cast<char*>(temp.c_str());
  }
%}
%typemap(freearg) char *, char const * ""

%typemap(out) char *, char const * %{
  if ($1) {
    std::size_t $1_len = std::strlen($1);
    $result = jenv->NewByteArray(static_cast<jsize>($1_len));
    if ($result) {
      jenv->SetByteArrayRegion($result, 0, static_cast<jsize>($1_len), reinterpret_cast<jbyte const*>($1));
    }
  }
%}

%typemap(directorin, descriptor="Ljava/lang/String;") char *, char const * %{
  $input = nullptr;
  if ($1) {
    std::size_t $1_len = std::strlen($1);
    $input = jenv->NewByteArray(static_cast<jsize>($1_len));
    if ($input) {
      jenv->SetByteArrayRegion($input, 0, static_cast<jsize>($1_len), reinterpret_cast<jbyte const*>($1));
    }
  }
  Swig::LocalRefGuard $1_refguard(jenv, $input);
%}

%typemap(javadirectorin) char *, char const *
    "($jniinput == null ? null : new String($jniinput, java.nio.charset.StandardCharsets.UTF_8))"

%typemap(directorout) char *, char const * %{
#error "jlibtorrent: add a byte[] directorout typemap for char* before returning one from a Java override"
%}
%typemap(javadirectorout) char *, char const * "$javacall"

// String constants from version.hpp: emit them as Java literals, since a constant's initializer
// calls the JNI getter directly and wouldn't decode the byte[].
%javaconst(1) LIBTORRENT_VERSION;
%javaconst(1) LIBTORRENT_REVISION;
