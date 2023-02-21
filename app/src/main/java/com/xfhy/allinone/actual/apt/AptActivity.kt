package com.xfhy.allinone.actual.apt

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2021/8/31 07:11
 * Description : APT
 * Annotation Processing Tool  注解处理器
 * APT是一种用来处理注解的工具,它会从源代码里面找到注解,然后通过注解来处理一些逻辑.
 */
class AptActivity : TitleBarActivity() {

    override fun getThisTitle() = "APT"

//    @BindView(R.id.tv_apt_test)
//    lateinit var tvAptTest: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apt)

        //生成的AptActivityBinding文件在
        // AllInOne/app/build/generated/source/kapt/debug/com/xfhy/allinone/actual/apt 文件夹里面
//        Binding.bind(this)

//        tvAptTest.text = "卧槽,成功了"
    }

}
