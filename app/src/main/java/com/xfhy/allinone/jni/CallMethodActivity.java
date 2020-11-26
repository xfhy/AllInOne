package com.xfhy.allinone.jni;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xfhy.allinone.R;
import com.xfhy.library.basekit.activity.TitleBarActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CallMethodActivity extends TitleBarActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @NotNull
    @Override
    public CharSequence getThisTitle() {
        return "JNI CallMethod";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_method);

        //callJavaInstanceMethod(new MyClass());
        //createAndCallJavaInstanceMethod();
        callJavaStaticMethod();
        sayHello();
    }

    /**
     * 这个方法是动态注册的
     */
    private native void sayHello();

    /**
     * 在native层调用某个类的static方法
     */
    private native void callJavaStaticMethod();

    /**
     * 在native层创建该对象的实例,然后调用该对象实例的方法
     */
    private native void createAndCallJavaInstanceMethod();

    /**
     * 这里传入对象实例,然后在native层调用该对象的方法
     */
    private native void callJavaInstanceMethod(MyJNIClass myClass);

    private native ArrayList testMaxQuote();

    public void callNormalMethod(View view) {
        callJavaInstanceMethod(new MyJNIClass());
    }

    public void callStaticMethod(View view) {
        callJavaStaticMethod();
    }

    public void createAndCallNormalMethod(View view) {
        createAndCallJavaInstanceMethod();
    }

    public void testMaxQuoteInNativeLocal(View view) {
        ArrayList arrayList = testMaxQuote();
        Log.e("xfhy", arrayList.get(45).toString());
    }


    public void nativeCrash(View view) {
        nativeCrashTest();
    }

    private native void nativeCrashTest();

}
