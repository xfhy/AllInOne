#include <jni.h>
#include <string>
#include <malloc.h>

void testMallocMethod() {
    //88*1024*1024=92274688
    malloc(88*1024*1024);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_nativelib_TestMalloc_testMalloc(JNIEnv *env, jobject thiz) {
    testMallocMethod();
}