package com.xfhy.allinone.view

import android.os.Bundle
import com.xfhy.allinone.databinding.ActivityViewHomeBinding
import com.xfhy.allinone.view.adaptation.ScreenAdaptActivity
import com.xfhy.allinone.view.fps.FpsActivity
import com.xfhy.allinone.view.request.RequestLayoutActivity
import com.xfhy.allinone.view.viewanimation.ViewAnimationActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2021/4/1 13:59
 * Description :
 */
class ViewHomeActivity : TitleBarActivity() {

    override fun getThisTitle() = "View"

    private lateinit var viewMainBinding: ActivityViewHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewbinding的原理就是AS自动为每个布局生成一个类，然后需要手动传进去一个LayoutInflater实例，在里面会完成加载布局，完成findViewById，定义view对应的变量，view对应的getter方法。
        viewMainBinding = ActivityViewHomeBinding.inflate(layoutInflater)
        setContentView(viewMainBinding.root)

        viewMainBinding.btnGoFps.setOnClickListener {
            startActivity<FpsActivity>()
        }
        viewMainBinding.btnGoScreenAdapt.setOnClickListener {
            startActivity<ScreenAdaptActivity>()
        }
        viewMainBinding.btnGoRequestLayout.setOnClickListener {
            startActivity<RequestLayoutActivity>()
        }
        viewMainBinding.btnGoViewAnimation.setOnClickListener {
            startActivity<ViewAnimationActivity>()
        }
    }

}