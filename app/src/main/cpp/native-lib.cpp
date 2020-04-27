#include <jni.h>
#include <string>
#include <android/log.h>


#define LOG_TAG "xfhy"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define DEBUG
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#ifdef DEBUG
#else
#define LOGI(...)
#endif

//JNIEnv* 表示一个纸箱JNI环境的指针,可以通过它来访问JNI提供的接口方法
//jobject 表示Java对象中的this
extern "C" JNIEXPORT jstring JNICALL
Java_com_xfhy_allinone_jni_JNIMainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    printf("invoke get in c++\\n");
    std::string hello = "Hello from C++";
    LOGI("测试测试测试小测试");
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_allinone_jni_JNIMainActivity_setString(JNIEnv *env, jobject thiz, jstring str) {
    char *content = (char *) env->GetStringUTFChars(str, NULL);
    LOGI("%s", content);

    //释放
    env->ReleaseStringUTFChars(str, content);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_allinone_jni_JNIMainActivity_callJavaMethod(JNIEnv *env, jobject thiz) {
    //1. 找到这个类的class
    jclass clazz = env->GetObjectClass(thiz);
    if (clazz == NULL) {
        LOGI("find class MainActivity error!");
        return;
    }

    //2. 再找到这个类里面的方法
    jmethodID id = env->GetMethodID(clazz, "javaMethod", "(Ljava/lang/String;)V");
    if (id == NULL) {
        LOGI("get method id is null");
        return;
    }

    //3. 生成字符串,调用该方法
    jstring str = env->NewStringUTF("I'm native string");
    env->CallVoidMethod(thiz, id, str);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_allinone_jni_JNIMainActivity_staticMethodTest(JNIEnv *env, jclass clazz) {

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xfhy_allinone_jni_JNIMainActivity_operateString(JNIEnv *env, jobject thiz, jstring str) {
    //从java的内存中把字符串弄出来  在native使用
    const char *strFromJava = (char *) env->GetStringUTFChars(str, NULL);
    if (strFromJava == NULL) {
        //必须空检查
        return NULL;
    }

    //将strFromJava拷贝到buff中,待会儿好拿去生成字符串
    char buff[128] = {0};
    strcpy(buff, strFromJava);
    strcat(buff, " 在字符串后面加点东西");

    //释放资源
    env->ReleaseStringUTFChars(str, strFromJava);

    //方式2  用GetStringUTFRegion方法将JVM中的字符串拷贝到C/C++的缓冲区中
    /*int len = env->GetStringLength(str);
    char buff[128];
    env->GetStringUTFRegion(str, 0, len, buff);
    LOGI("-------------- %s", buff);*/

    //自动转为Unicode
    return env->NewStringUTF(buff);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_xfhy_allinone_jni_JNIMainActivity_sumArray(JNIEnv *env, jobject thiz, jintArray array) {
    //数组求和
    int result = 0;

    //方式1
    //此种方式比较危险,GetIntArrayElements会直接获取数组元素指针,是可以直接对该数组元素进行修改的.
    jint *c_arr = env->GetIntArrayElements(array, NULL);
    if (c_arr == NULL) {
        return 0;
    }
    c_arr[0] = 15;
    jint len = env->GetArrayLength(array);
    for (int i = 0; i < len; ++i) {
        //result += *(c_arr + i); 写成这种形式,或者下面一行那种都行
        result += c_arr[i];
    }
    //有Get,一般就有Release
    env->ReleaseIntArrayElements(array, c_arr, 0);

    //方式2  推荐使用
    jint arr_len = env->GetArrayLength(array);
    //动态申请数组
    jint *c_array = (jint *) malloc(arr_len * sizeof(jint));
    //初始化数组元素内容为0
    memset(c_array, 0, sizeof(jint) * arr_len);
    //将java数组的[0-arr_len)位置的元素拷贝到c_array数组中
    env->GetIntArrayRegion(array, 0, arr_len, c_array);
    for (int i = 0; i < arr_len; ++i) {
        result += c_array[i];
    }
    //动态申请的内存 必须释放
    free(c_array);

    return result;
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_xfhy_allinone_jni_JNIMainActivity_init2DArray(JNIEnv *env, jobject thiz, jint size) {
    //创建一个size*size大小的二维数组

    //jobjectArray是用来装对象数组的   Java数组就是一个对象 int[]
    jclass classIntArray = env->FindClass("[I");
    if (classIntArray == NULL) {
        return NULL;
    }
    //创建一个数组对象,元素为classIntArray
    jobjectArray result = env->NewObjectArray(size, classIntArray, NULL);
    if (result == NULL) {
        return NULL;
    }
    for (int i = 0; i < size; ++i) {
        jint buff[100];
        //创建第二维的数组 是第一维数组的一个元素
        jintArray intArr = env->NewIntArray(size);
        if (intArr == NULL) {
            return NULL;
        }
        for (int j = 0; j < size; ++j) {
            //这里随便设置一个值
            buff[j] = 666;
        }
        //给一个jintArray设置数据
        env->SetIntArrayRegion(intArr, 0, size, buff);
        //给一个jobjectArray设置数据 第i索引,数据位intArr
        env->SetObjectArrayElement(result, i, intArr);
        //及时移除引用
        env->DeleteLocalRef(intArr);
    }

    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_allinone_jni_CallMethodActivity_callJavaInstanceMethod(JNIEnv *env, jobject thiz,
                                                                     jobject my_class) {
    jclass clazz = env->GetObjectClass(my_class);
    //AS有提示,输入getAge按enter就可以自动补全
    jmethodID jmethodId = env->GetMethodID(clazz, "getAge", "()I");
    jint age = env->CallIntMethod(my_class, jmethodId);
    LOGI("age = %d", age);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_allinone_jni_CallMethodActivity_createAndCallJavaInstanceMethod(JNIEnv *env,
                                                                              jobject thiz) {
    //构建一个类的实例,并调用该实例的方法

    //特别注意 : 正常情况下,这里的每一个获取出来都需要判空处理,我这里为了展示核心代码,就不判空了
    //这里输入MyClass之后AS会提示,然后按Enter键即可自动补全
    jclass clazz = env->FindClass("com/xfhy/allinone/jni/MyJNIClass");
    //获取构造方法的方法id
    jmethodID mid_construct = env->GetMethodID(clazz, "<init>", "()V");
    //获取getAge方法的方法id
    jmethodID mid_get_age = env->GetMethodID(clazz, "getAge", "()I");
    jmethodID mid_set_age = env->GetMethodID(clazz, "setAge", "(I)V");
    jobject jobj = env->NewObject(clazz, mid_construct);

    //调用方法setAge
    env->CallVoidMethod(jobj, mid_set_age, 20);
    //再调用方法getAge 获取返回值 打印输出
    jint age = env->CallIntMethod(jobj, mid_get_age);
    LOGI("获取到 age = %d", age);

    //凡是使用是jobject的子类,都需要移除引用
    env->DeleteLocalRef(clazz);
    env->DeleteLocalRef(jobj);

    /*if (clazz == NULL) {
        return;
    }
    if (mid_construct == NULL) {
        return;
    }
    if (mid_get_age == NULL) {
        return;
    }*/
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_allinone_jni_CallMethodActivity_callJavaStaticMethod(JNIEnv *env, jobject thiz) {
    //调用某个类的static方法

    //JVM使用一个类时,是需要先判断这个类是否被加载了,如果没被加载则还需要加载一下才能使用
    //1. 从classpath路径下搜索MyJNIClass这个类,并返回该类的Class对象
    jclass clazz = env->FindClass("com/xfhy/allinone/jni/MyJNIClass");
    //2. 从clazz类中查找getDes方法 得到这个静态方法的方法id
    jmethodID mid_get_des = env->GetStaticMethodID(clazz, "getDes",
                                                   "(Ljava/lang/String;)Ljava/lang/String;");
    //3. 构建入参,调用static方法,获取返回值
    jstring str_arg = env->NewStringUTF("我是xfhy");
    jstring result = (jstring) env->CallStaticObjectMethod(clazz, mid_get_des, str_arg);
    const char *result_str = env->GetStringUTFChars(result, NULL);
    LOGI("获取到Java层返回的数据 : %s", result_str);

    //4. 移除局部引用
    env->DeleteLocalRef(clazz);
    env->DeleteLocalRef(str_arg);
    env->DeleteLocalRef(result);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_xfhy_allinone_jni_CallMethodActivity_testMaxQuote(JNIEnv *env, jobject thiz) {
    //测试Android虚拟机引用表最大限度数量
    //局部引用溢出在Android不同版本上表现有所区别.Android 8.0之前局部引用表的上限是512个引用,Android 8.0之后局部引用表上限提升到了8388608个引用.
    //而Oracle Java没有局部引用表上限限制,随着局部引用表不断增大,最终会OOM.

    jclass clazz = env->FindClass("java/util/ArrayList");
    jmethodID constrId = env->GetMethodID(clazz, "<init>", "(I)V");
    jmethodID addId = env->GetMethodID(clazz, "add", "(ILjava/lang/Object;)V");
    jobject arrayList = env->NewObject(clazz, constrId, 513);
    for (int i = 0; i < 513; ++i) {
        jstring test_str = env->NewStringUTF("test");
        env->CallVoidMethod(arrayList, addId, 0, test_str);
        //这里应该删除的 !!!
        //env->DeleteLocalRef(test_str);
    }

    return arrayList;
}