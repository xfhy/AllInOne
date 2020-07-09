package com.xfhy.allinone.smali;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xfhy.allinone.R;

/**
 * @author : xfhy
 * Create time : 2020/6/14 3:18 PM
 * Description : smali 语法学习
 */
public class SmaliActivity extends AppCompatActivity {

    private int num;
    private TextView tvName;
    public String text = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smail);

        initView();
        getNum(1, "", false);
        getDouble();
        switchTest(3);
        showText(false);
    }

    private void showText(boolean isVip) {
        if (isVip) {
            Toast.makeText(this, "Skip ad", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Watch ad", Toast.LENGTH_SHORT).show();
        }
    }

    private int switchTest(int num) {
        int result = 8;
        switch (num) {
            case 1:
                result = 10;
                break;
            case 2:
                result = 12;
                break;
            case 3:
                result = 14;
                break;
            default:
                break;
        }
        return result;
    }

    private void initView() {
        int num = 2 + 3;
        String name = "zhangsan";
    }

    public String getNum(int a, String b, boolean c) {
        if (a > 1) {
            return "aaaa";
        }
        int num = 1 + 1;
        return "";
    }

    public double getDouble() {
        return 0.0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showMyText(View view) {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }
}
