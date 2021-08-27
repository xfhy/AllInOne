package com.xfhy.allinone.actual.hotfix

import com.xfhy.allinone.App
import com.xfhy.allinone.actual.plugin.InsertDexUtils
import com.xfhy.library.ext.log
import com.xfhy.library.utils.FieldUtil
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream

/**
 * @author : xfhy
 * Create time : 2021/8/27 10:01 下午
 * Description : 热修复工具类
 */
object HotFixUtil {
    fun loadPatch() {
        val appContext = App.getAppContext()
        //HotFixUtil::class.java.classLoader 即 PathClassLoader,默认的类加载器
        //1. PathClassLoader->BaseDexClassLoader 拿到当前程序的dexElements数组
        val oldPathList = FieldUtil.getFieldValue(BaseDexClassLoader::class.java, HotFixUtil::class.java.classLoader, "pathList")
        val DexPathListClazz = Class.forName("dalvik.system.DexPathList")
        val oldDexElements = FieldUtil.getFieldValue(DexPathListClazz, oldPathList, "dexElements")

        //2. 补丁包  构建一个DexClassLoader 用来加载补丁包里面的class
        val inputStream = appContext.assets.open("hotfix.dex")
        val file = File("${appContext.cacheDir}/hotfix.dex")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val dexClassLoader = DexClassLoader(file.path, appContext.cacheDir.path, null, HotFixUtil::class.java.classLoader)
        val newPathList = FieldUtil.getFieldValue(BaseDexClassLoader::class.java, dexClassLoader, "pathList")
        val newDexElements = FieldUtil.getFieldValue(DexPathListClazz, newPathList, "dexElements")

        //3. 合并2个数组
        val combineArray = InsertDexUtils.combineArray(newDexElements, oldDexElements)

        //4. 将合并好的数组放进当前程序的BaseDexClassLoader的dexElements数组中,这样在找class时就先找到补丁里面的class,热修复成功
        FieldUtil.setField(DexPathListClazz, oldPathList!!, "dexElements", combineArray)

        log("热修复成功")
    }

}