// Bulk byte[] <-> std::vector<std::int8_t> copies, one JNI call each, for Vectors
// (frostwire/frostwire-jlibtorrent#338). Vectors used to copy one byte per JNI call:
// about 10 ms per megabyte round trip.

// A Java byte[] read by native code: the elements are released with JNI_ABORT (no copy back).
%typemap(jni) (std::int8_t const* JBYTES_IN, std::size_t JLENGTH) "jbyteArray"
%typemap(jtype) (std::int8_t const* JBYTES_IN, std::size_t JLENGTH) "byte[]"
%typemap(jstype) (std::int8_t const* JBYTES_IN, std::size_t JLENGTH) "byte[]"
%typemap(javain) (std::int8_t const* JBYTES_IN, std::size_t JLENGTH) "$javainput"
%typemap(in) (std::int8_t const* JBYTES_IN, std::size_t JLENGTH) %{
  if (!$input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null byte array");
    return $null;
  }
  $2 = static_cast<std::size_t>(jenv->GetArrayLength($input));
  $1 = ($1_ltype) jenv->GetByteArrayElements($input, nullptr);
  if (!$1) return $null;
%}
%typemap(freearg) (std::int8_t const* JBYTES_IN, std::size_t JLENGTH) %{
  jenv->ReleaseByteArrayElements($input, (jbyte*) $1, JNI_ABORT);
%}

// A Java byte[] written by native code: released with mode 0, which copies the bytes back.
%typemap(jni) (std::int8_t* JBYTES_OUT, std::size_t JLENGTH) "jbyteArray"
%typemap(jtype) (std::int8_t* JBYTES_OUT, std::size_t JLENGTH) "byte[]"
%typemap(jstype) (std::int8_t* JBYTES_OUT, std::size_t JLENGTH) "byte[]"
%typemap(javain) (std::int8_t* JBYTES_OUT, std::size_t JLENGTH) "$javainput"
%typemap(in) (std::int8_t* JBYTES_OUT, std::size_t JLENGTH) %{
  if (!$input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null byte array");
    return $null;
  }
  $2 = static_cast<std::size_t>(jenv->GetArrayLength($input));
  $1 = ($1_ltype) jenv->GetByteArrayElements($input, nullptr);
  if (!$1) return $null;
%}
%typemap(freearg) (std::int8_t* JBYTES_OUT, std::size_t JLENGTH) %{
  jenv->ReleaseByteArrayElements($input, (jbyte*) $1, 0);
%}

%extend std::vector<std::int8_t> {
    // Replaces the contents with a copy of the Java array.
    void assign_bytes(std::int8_t const* JBYTES_IN, std::size_t JLENGTH) {
        $self->assign(JBYTES_IN, JBYTES_IN + JLENGTH);
    }

    // Copies min(size(), array length) bytes into the Java array.
    void copy_to(std::int8_t* JBYTES_OUT, std::size_t JLENGTH) {
        std::size_t const n = std::min($self->size(), JLENGTH);
        if (n > 0) std::memcpy(JBYTES_OUT, $self->data(), n);
    }
}
