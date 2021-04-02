package com.xfhy.allinone.view.fps

import android.annotation.SuppressLint
import android.os.Bundle
import com.xfhy.allinone.databinding.ActivityFpsBinding
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2021/4/1 13:59
 * Description : FPS 测量
 */
class FpsActivity : TitleBarActivity() {

    private lateinit var fpsBinding: ActivityFpsBinding

    override fun getThisTitle() = "FPS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fpsBinding = ActivityFpsBinding.inflate(layoutInflater)
        setContentView(fpsBinding.root)
        doOnFrame()
    }


    @SuppressLint("SetTextI18n")
    private fun doOnFrame() {

        FpsMonitor.startMonitor {
            fpsBinding.tvFps.text = "${it}fps"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        FpsMonitor.stopMonitor()
    }

}