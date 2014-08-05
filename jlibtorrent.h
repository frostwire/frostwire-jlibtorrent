#ifndef _JLIBTORRENT_H
#define _JLIBTORRENT_H

#define JNI_METHOD(clazz, type, name, ...) \
    JNIEXPORT type JNICALL Java_com_frostwire_libtorrent_##clazz##_##name(JNIEnv*, jobject, ##__VA_ARGS__);

#define JNI_METHOD_BEGIN(clazz, type, name, ...) \
    JNIEXPORT type JNICALL Java_com_frostwire_libtorrent_##clazz##_##name(JNIEnv* env, jobject obj, ##__VA_ARGS__) {

#define JNI_METHOD_END \
    }

#endif
