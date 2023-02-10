#include <jni.h>
#include <string>
#include <malloc.h>

void my_malloc() {
    malloc(88*1024*1024);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_nativelib_TestMalloc_testMalloc(JNIEnv *env, jobject thiz) {
    my_malloc();
}