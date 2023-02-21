package com.xfhy.asm.timecost

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

/**
 * @author : xfhy
 * Create time : 2023/2/20 20:46
 * Description : AmsClassVisitorFactory是用于创建ClassVisitor对象的工厂，必须定义为抽象类
 */
abstract class TimeCostTransform :AsmClassVisitorFactory<InstrumentationParameters.None>{

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        //createClassVisitor返回我们自定义的ClassVisitor,在自定义Visitor处理完成后，需要传内容给下一个Visitor，因此会将其放在构造函数中传入
        return TimeCostClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        //用于控制我们自定义的Visitor是否需要处理这个类，通过这个方法可以过滤我们不需要的类，加快编译速度
        return true
    }

}