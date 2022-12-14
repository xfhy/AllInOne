package com.xfhy.allinone.performance

import com.xfhy.allinone.base.BaseDemoActivity
import com.xfhy.allinone.performance.caton.CatonDetectionActivity
import com.xfhy.allinone.performance.memory.MemoryOptActivity
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2021/4/3 6:40 AM
 * Description :
 */
class PerformanceHomeActivity : BaseDemoActivity() {

    override fun getThisTitle() = "性能优化"

    override fun initButtons() {
        addButtonItem("卡顿检测") {
            startActivity<CatonDetectionActivity>()
        }
        addButtonItem("内存优化") {
            startActivity<MemoryOptActivity>()
        }
    }
}