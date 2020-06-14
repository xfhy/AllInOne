package com.xfhy.allinone.smali;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xfhy.allinone.R;

/**
 * @author : xfhy
 * Create time : 2020/6/14 3:18 PM
 * Description : smali 语法学习
 */
public class SmaliActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smail);

        initView();
    }

    private void initView() {
        int num = 2 + 3;
        String name = "zhangsan";
        Log.w("xfhy666", "initView: num = " + num + "  name = " + name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
