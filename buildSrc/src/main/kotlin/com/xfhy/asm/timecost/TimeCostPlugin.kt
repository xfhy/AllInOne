package com.xfhy.asm.timecost

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author : xfhy
 * Create time : 2023/2/20 21:20
 * Description : 方法耗时统计 , ASM插桩
 *
 * Gradle 7.0 开始是通过 AndroidComponentsExtension来注册脚本的，之前的Transform被标记为废弃
 *
 */
class TimeCostPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            //不同的variant可以实现不同的处理
            //InstrumentationScope控制扫描哪些代码
            variant.instrumentation.transformClassesWith(
                TimeCostTransform::class.java,
                InstrumentationScope.PROJECT
            ) {}
            //设置不同的栈帧计算模式
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
        }
    }
}