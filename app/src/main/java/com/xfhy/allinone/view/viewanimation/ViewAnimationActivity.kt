package com.xfhy.allinone.view.viewanimation

import android.os.Bundle
import android.view.View
import android.view.animation.ScaleAnimation
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_view_animation.*

/**
 * @author : xfhy
 * Create time : 2022/8/29 6:44 上午
 * Description : View动画
 */
class ViewAnimationActivity : TitleBarActivity() {

    override fun getThisTitle() = "View动画"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation)

    }

    fun startAnimation(view: View) {
        val scaleAnimation = ScaleAnimation(0f, 1f, 0f, 1f)
        scaleAnimation.duration = 1000
        scaleAnimation.fillAfter = true
        iv_animation_img.startAnimation(scaleAnimation)
    }

}