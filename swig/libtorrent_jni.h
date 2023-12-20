/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

#ifndef SWIG_libtorrent_WRAP_H_
#define SWIG_libtorrent_WRAP_H_

struct SwigDirector_alert_notify_callback : public alert_notify_callback, public Swig::Director {

public:
    void swig_connect_director(JNIEnv *jenv, jobject jself, jclass jcls, bool swig_mem_own, bool weak_global);
    SwigDirector_alert_notify_callback(JNIEnv *jenv);
    virtual ~SwigDirector_alert_notify_callback();
    virtual void on_alert();
public:
    bool swig_overrides(int n) {
      return (n < 1 ? swig_override[n] : false);
    }
protected:
    Swig::BoolArray<1> swig_override;
};

struct SwigDirector_add_files_listener : public add_files_listener, public Swig::Director {

public:
    void swig_connect_director(JNIEnv *jenv, jobject jself, jclass jcls, bool swig_mem_own, bool weak_global);
    SwigDirector_add_files_listener(JNIEnv *jenv);
    virtual ~SwigDirector_add_files_listener();
    virtual bool pred(std::string const &p);
public:
    bool swig_overrides(int n) {
      return (n < 1 ? swig_override[n] : false);
    }
protected:
    Swig::BoolArray<1> swig_override;
};

struct SwigDirector_set_piece_hashes_listener : public set_piece_hashes_listener, public Swig::Director {

public:
    void swig_connect_director(JNIEnv *jenv, jobject jself, jclass jcls, bool swig_mem_own, bool weak_global);
    SwigDirector_set_piece_hashes_listener(JNIEnv *jenv);
    virtual ~SwigDirector_set_piece_hashes_listener();
    virtual void progress(int i);
public:
    bool swig_overrides(int n) {
      return (n < 1 ? swig_override[n] : false);
    }
protected:
    Swig::BoolArray<1> swig_override;
};

struct SwigDirector_posix_wrapper : public posix_wrapper, public Swig::Director {

public:
    void swig_connect_director(JNIEnv *jenv, jobject jself, jclass jcls, bool swig_mem_own, bool weak_global);
    SwigDirector_posix_wrapper(JNIEnv *jenv);
    virtual ~SwigDirector_posix_wrapper();
    virtual int open(char const *path,int flags,int mode);
    virtual int stat(char const *path,posix_stat_t *buf);
    virtual int mkdir(char const *path,int mode);
    virtual int rename(char const *oldpath,char const *newpath);
    virtual int remove(char const *path);
public:
    bool swig_overrides(int n) {
      return (n < 5 ? swig_override[n] : false);
    }
protected:
    Swig::BoolArray<5> swig_override;
};


#endif
