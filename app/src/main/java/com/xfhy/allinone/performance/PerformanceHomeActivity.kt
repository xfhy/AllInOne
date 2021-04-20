package com.xfhy.allinone.performance

import android.os.Bundle
import com.xfhy.allinone.databinding.ActivityPerformanceHomeBinding
import com.xfhy.allinone.performance.caton.CatonDetectionActivity
import com.xfhy.allinone.performance.memory.MemoryLeakActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2021/4/3 6:40 AM
 * Description :
 */
class PerformanceHomeActivity : TitleBarActivity() {

    private lateinit var performanceHomeBinding: ActivityPerformanceHomeBinding
    override fun getThisTitle() = "性能优化"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceHomeBinding = ActivityPerformanceHomeBinding.inflate(layoutInflater)
        setContentView(performanceHomeBinding.root)

        performanceHomeBinding.btnDetectCaton.setOnClickListener {
            startActivity<CatonDetectionActivity>()
        }
        performanceHomeBinding.btnMemoryLeak.setOnClickListener {
            startActivity<MemoryLeakActivity>()
        }
    }

}