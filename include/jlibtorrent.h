#ifndef _JLIBTORRENT_H
#define _JLIBTORRENT_H

#include <JavaVM/jni.h>

// BEGIN JNI MACROS -----------------------------------------------

#define METHOD_NAME_HELPER(x, y) Java_com_frostwire_libtorrent_##x##_##y
#define METHOD_NAME(x, y) METHOD_NAME_HELPER(x, y)

#define JNI_METHOD(type, name, ...) \
    JNIEXPORT type JNICALL METHOD_NAME(JNI_CLASS_NAME, name)(JNIEnv*, jobject, ##__VA_ARGS__);

#define JNI_METHOD_BEGIN(type, name, ...) \
    JNIEXPORT type JNICALL METHOD_NAME(JNI_CLASS_NAME, name)(JNIEnv *env, jobject obj, ##__VA_ARGS__) {

#define JNI_METHOD_END \
    }

#define JNI_ARRAY_FOREACH_BEGIN(arr, type, name) \
    int arrLength = env->GetArrayLength(arr);\
    \
    for (int arrIndex = 0; arrIndex < arrLength; arrIndex++) {\
        type name = (type) env->GetObjectArrayElement(arr, arrIndex);


#define JNI_ARRAY_FOREACH_END \
    }

#define JNI_GET_STRING(str, name) \
    jboolean isStrCopy; \
    const char *name = env->GetStringUTFChars(str, &isStrCopy);

#define JNI_RELEASE_STRING(str, name) \
    env->ReleaseStringUTFChars(str, name);

#define JNI_STRING_ARRAY_FOREACH_BEGIN(arr, name) \
    JNI_ARRAY_FOREACH_BEGIN(arr, jstring, arrElement) \
        JNI_GET_STRING(arrElement, name)

#define JNI_STRING_ARRAY_FOREACH_END(name) \
    JNI_RELEASE_STRING(arrElement, name) \
    JNI_ARRAY_FOREACH_END

#define JNI_NEW_ARRAY(type, length, name) \
    jobjectArray name = env->NewObjectArray(length, env->FindClass(type), NULL);

#define JNI_NEW_STRING_ARRAY(length, name) JNI_NEW_ARRAY("java/lang/String", length, name)

#define JNI_ARRAY_SET(arr, indx, obj) env->SetObjectArrayElement(arr, indx, obj);

// END JNI MACROS -----------------------------------------------

#define BOOST_ASIO_SEPARATE_COMPILATION

#include <string>

using namespace std;

#include <libtorrent/version.hpp>
#include <libtorrent/file_storage.hpp>
#include <libtorrent/create_torrent.hpp>
#include <libtorrent/session.hpp>
#include <libtorrent/alert.hpp>

using namespace libtorrent;

#endif //_JLIBTORRENT_H
