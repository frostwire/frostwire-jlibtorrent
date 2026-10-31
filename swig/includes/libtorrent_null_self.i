// A method called on a Java proxy after delete() passes a null pointer as `self`. Without this
// check the native method dereferences it and the JVM dies with SIGSEGV; with it, the call throws
// IllegalStateException naming the type. One pointer comparison per call.
//
// fetchMagnet freed its error_code and then called ec.clear() on it (2.0.15.0-2.0.15.2): every
// magnet fetch of a new torrent crashed the JVM in error_code_clear.

%typemap(check) SWIGTYPE *self %{
  if (!$1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaIllegalStateException, "$1_basetype used after delete()");
    return $null;
  }
%}

%typemap(check) SWIGTYPE const *self = SWIGTYPE *self;
