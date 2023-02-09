#include <jni.h>
#include <string>
#include <malloc.h>

void my_malloc() {
    malloc(400*1024*1024);
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_xfhy_nativelib_TestMalloc_testMalloc(JNIEnv *env, jobject thiz) {
    my_malloc();
    return env->NewIntArray(50*1024*1024);
}