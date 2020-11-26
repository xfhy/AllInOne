package com.xfhy.allinone.jni;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xfhy.allinone.R;
import com.xfhy.library.basekit.activity.TitleBarActivity;

import org.jetbrains.annotations.NotNull;

public class JNIMainActivity extends TitleBarActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private TextView mTv;

    @NotNull
    @Override
    public CharSequence getThisTitle() {
        return "JNI Test";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_main);

        // Example of a call to a native method
        mTv = findViewById(R.id.sample_text);
        mTv.setText(stringFromJNI());

        //setString("Java中的字符串");
//        callJavaMethod();
//        String result = operateString("待操作的字符串");

        /*int[] array = {1, 2, 3, 4};
        Log.d("xfhy", "数组之和 : " + sumArray(array));
        for (int i = 0; i < array.length; i++) {
            Log.d("xfhy", "数组 i:" + i + "  数据:" + array[i]);
        }*/

        int[][] init2DArray = init2DArray(3);
        for (int i = 0; i < 3; i++) {
            for (int i1 = 0; i1 < 3; i1++) {
                Log.d("xfhy", "init2DArray[" + i + "][" + i1 + "]" + " = " + init2DArray[i][i1]);
            }
        }
    }

    /**
     * 从C层拿字符串
     */
    public native String stringFromJNI();

    /**
     * 给native传递一个字符串
     */
    public native void setString(String str);

    /**
     * 调用native层方法,然后让native调用java方法
     */
    public native void callJavaMethod();

    public static native void staticMethodTest();

    /**
     * native操作字符串
     */
    public native String operateString(String str);

    /**
     * 数组求和
     */
    public native int sumArray(int[] array);

    /**
     * 让native初始化二维数组
     */
    public native int[][] init2DArray(int size);

    /**
     * native层将调用这个方法
     */
    @SuppressLint("SetTextI18n")
    public void javaMethod(String str) {
        mTv.setText("我是java方法 里面的log 入参:" + str);
    }

    public void btnGoCallMethod(View view) {
        startActivity(new Intent(this, CallMethodActivity.class));
    }
}
