package com.xfhy.library.utils

import java.lang.reflect.Field

/**
 * @author : xfhy
 * Create time : 2021/8/18 9:34 下午
 * Description : 反射工具类
 */
object FieldUtil {

    fun getFieldValue(clazz: Class<*>, target: Any?, fieldName: String): Any? {
        val declaredField = clazz.getDeclaredField(fieldName)
        declaredField.isAccessible = true
        return declaredField.get(target)
    }

    fun getField(clazz: Class<*>, name: String): Field {
        val declaredField = clazz.getDeclaredField(name)
        declaredField.isAccessible = true
        return declaredField
    }

    fun setField(clazz: Class<*>, target: Any, name: String, value: Any?) {
        val field = getField(clazz, name)
        field.set(target, value)
    }

}