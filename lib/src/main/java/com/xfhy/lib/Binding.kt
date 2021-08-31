package com.xfhy.lib

import android.app.Activity

/**
 * @author : xfhy
 * Create time : 2021/8/31 7:28 上午
 * Description :
 */
object Binding {
    fun bind(activity: Activity) {
        //这是APT编译时生成的XXBinding  XX是Activity的名字
        val canonicalName = "${activity::class.java.canonicalName}Binding"
        val clazz = Class.forName(canonicalName)
        val declaredConstructor = clazz.getDeclaredConstructor(activity::class.java)
        declaredConstructor.isAccessible = true
        //只需要构建出来就行   在XXBinding的构造方法里面会初始化XX这个Activity的所有含有@BindView注解的属性
        val newInstance = declaredConstructor.newInstance(activity)
        println("test")

        
        //每个XXBinding只会反射一次,几乎没有性能损耗
    }
}