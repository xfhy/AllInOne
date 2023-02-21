package com.xfhy.asm.timecost

import groovyjarjarasm.asm.Opcodes
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author : xfhy
 * Create time : 2023/2/20 20:52
 * Description :
 */
class TimeCostClassVisitor(nextClassVisitor: ClassVisitor, private val className: String) :
    ClassVisitor(Opcodes.ASM5, nextClassVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        val newMethodVisitor =
            object : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {
                override fun onMethodEnter() {
                    //方法开始
                    if (isNeedVisiMethod(name, className)) {
                        mv.visitLdcInsn(name)
                        mv.visitLdcInsn(className)
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            "com/xfhy/allinone/util/TimeCache",
                            "putStartTime",
                            "(Ljava/lang/String;Ljava/lang/String;)V",
                            false
                        )
                    }
                    super.onMethodEnter()
                }

                override fun onMethodExit(opcode: Int) {
                    //方法结束
                    if (isNeedVisiMethod(name, className)) {
                        mv.visitLdcInsn(name)
                        mv.visitLdcInsn(className)
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            "com/xfhy/allinone/util/TimeCache",
                            "putEndTime",
                            "(Ljava/lang/String;Ljava/lang/String;)V",
                            false
                        )
                    }
                    super.onMethodExit(opcode)
                }
            }
        return newMethodVisitor
    }

    private fun isNeedVisiMethod(name: String?, className: String?): Boolean {
        //putStartTime、putEndTime、printlnTime是TimeCache工具类里面的方法
        if (className.isNullOrBlank()) {
            return false
        }
        //<clinit> : jvm第一次加载class文件时调用,包括静态变量初始化语句和静态块的执行
        //<init> : 实例创建出来的时候调用. 场景: new 对象的时候、调用Class或Java.lang.reflect.Constructor对象的newInstance()方法、调用任何现有对象的clone()方法、通过java.io.ObjectInputStream类的getObject()方法反序列化。
        return name != "putStartTime" && name != "putEndTime" && name != "printlnTime" && name != "<clinit>" && name != "<init>"
    }


}