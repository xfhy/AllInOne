package com.xfhy.allinone.viewopt;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xfhy.allinone.R;

/**
 * @author : xfhy
 * Create time : 2020/6/2 15:03
 * Description : 去掉生成View过程中的反射逻辑 方案
 * 出自鸿神文章: https://mp.weixin.qq.com/s/ceXsH06fUFa7y4lzi4uXzw
 */
public class ViewOptActivity extends AppCompatActivity {

    public static final String TAG = "view_opt_activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_opt);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        //Log.w(TAG, "onCreateView111: name = " + name);
        if (name.contains("textview")) {
            TextView textView = new TextView(context, attrs);
            textView.setText("我被拦截了....");
            return textView;
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        Log.w(TAG, "onCreateView222: name = " + name);
        return super.onCreateView(name, context, attrs);
    }
}
