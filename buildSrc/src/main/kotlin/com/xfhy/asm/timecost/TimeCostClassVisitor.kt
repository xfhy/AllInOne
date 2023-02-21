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
class TimeCostClassVisitor(nextClassVisitor: ClassVisitor) :
    ClassVisitor(Opcodes.ASM5, nextClassVisitor) {

    private var className: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name
        println("className = $className , superName = $superName")
        super.visit(version, access, name, signature, superName, interfaces)
    }

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
                    try {
                        if (isNeedVisiMethod(name, className)) {
                            mv.visitLdcInsn(name)
                            mv.visitMethodInsn(
                                INVOKESTATIC,
                                "com/xfhy/allinone/util/TimeCache",
                                "putStartTime",
                                "(Ljava/lang/String;Ljava/lang/String;)V",
                                false
                            )
                        }

                    } catch (e: Throwable) {
                        println("-----------------------出错了--------------------")
                        e.printStackTrace()
                    } finally {
                        super.onMethodEnter()
                    }

                }

                override fun onMethodExit(opcode: Int) {
                    //方法结束
                    try {
                        if (isNeedVisiMethod(name, className)) {
                            mv.visitLdcInsn(name)
                            mv.visitMethodInsn(
                                INVOKESTATIC,
                                "com/xfhy/allinone/util/TimeCache",
                                "putEndTime",
                                "(Ljava/lang/String;Ljava/lang/String;)V",
                                false
                            )
                        }
                    } catch (e: Throwable) {
                        println("-----------------------出错了--------------------")
                        e.printStackTrace()
                    } finally {
                        super.onMethodExit(opcode)
                    }


                }
            }
        return newMethodVisitor
    }

    private fun isNeedVisiMethod(name: String?, className: String?): Boolean {
        //putStartTime、putEndTime、printlnTime是TimeCache工具类里面的方法
        if (className.isNullOrBlank()) {
            return false
        }
        if (!className.contains("xfhy")) {
            return false
        }
        if (className.contains("Activity")) {
            return false
        }
        return name != "putStartTime" && name != "putEndTime" && name != "printlnTime" && name != "<clinit>" && name != "<init>"
    }


}