#ifndef _JNI_UTIL_H
#define _JNI_UTIL_H

#include <JavaVM/jni.h>

#include <stdexcept>
#include <string>
#include <ios>

using namespace std;

#define METHOD_NAME_HELPER(x, y) Java_com_frostwire_libtorrent_##x##_##y
#define METHOD_NAME(x, y) METHOD_NAME_HELPER(x, y)

#define JNI_METHOD(type, name, ...) \
    JNIEXPORT type JNICALL METHOD_NAME(JNI_CLASS_NAME, name)(JNIEnv*, jobject, ##__VA_ARGS__);

#define JNI_METHOD_BEGIN(type, name, ...) \
    JNIEXPORT type JNICALL METHOD_NAME(JNI_CLASS_NAME, name)(JNIEnv *env, jobject obj, ##__VA_ARGS__) {\
        try {

#define JNI_METHOD_END \
        } catch (...) {\
            translate_cpp_exception(env);\
        }\
    }

#define JNI_METHOD_END_RET(type) \
        } catch (...) {\
            translate_cpp_exception(env);\
            return (type) NULL;\
        }\
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

struct ThrownJavaException : runtime_error {

    ThrownJavaException() : runtime_error("") {
    }

    ThrownJavaException(const string &msg) : runtime_error(msg) {
    }
};

struct NewJavaException : public ThrownJavaException {

    NewJavaException(JNIEnv *env, const char *type = "", const char *message = "")
            : ThrownJavaException(type + string(" ") + message) {
        jclass newExcCls = env->FindClass(type);
        if (newExcCls != NULL) {
            env->ThrowNew(newExcCls, message);
        }
    }
};

struct NewJavaError : public NewJavaException {

    NewJavaError(JNIEnv *env, const char *message = "")
            : NewJavaException(env, "java/lang/Error", message) {
    }
};

inline void assert_no_java_exception(JNIEnv *env) {
    if (env->ExceptionCheck() == JNI_FALSE) {
        throw ThrownJavaException("assert_no_java_exception");
    }
}

inline void translate_cpp_exception(JNIEnv *env) {
    try {
        throw;
    } catch (const ThrownJavaException &) {
        //already reported to Java, ignore
    } catch (const std::bad_alloc &rhs) {
        //translate OOM C++ exception to a Java exception
        NewJavaException(env, "java/lang/OutOfMemoryError", rhs.what());
    } catch (const ios_base::failure &rhs) { //sample translation
        //translate IO C++ exception to a Java exception
        NewJavaException(env, "java/io/IOException", rhs.what());
    } catch (const exception &e) {
        //translate unknown C++ exception to a Java exception
        NewJavaError(env, e.what());
    } catch (...) {
        //translate unknown C++ exception to a Java exception
        NewJavaError(env, "Unknown exception type");
    }
}

#endif //_JNI_UTIL_H
