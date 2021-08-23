package com.xfhy.allinone.actual.plugin;

import android.util.Log;

import com.xfhy.library.utils.FieldUtil;

import java.lang.reflect.Array;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @author : xfhy
 * Create time : 2021/8/23 7:46 上午
 * Description :
 */
public class InsertDexUtils {

    public static void injectDexAtFirst(String dexPath, String defaultDexOptPath) {
        try {
            // 创建补丁 dex 的 classloader，目的是使用其中的补丁 dexElements
            PathClassLoader pathClassLoader = getPathClassLoader();
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath, defaultDexOptPath, dexPath, pathClassLoader);
            // 获取到旧的 classloader 的 pathlist.dexElements 变量
            Object pathList = getPathList(pathClassLoader);
            Object baseDexElements = getDexElements(pathList);
            // 获取到补丁 classloader 的 pathlist.dexElements 变量
            Object newDexElements = getDexElements(getPathList(dexClassLoader));
            // 将补丁 的 dexElements 插入到旧的 classloader.pathlist.dexElements 前面
            Object allDexElements = combineArray(newDexElements, baseDexElements);
            //将生成的数组 存到dexElements中
            FieldUtil.INSTANCE.setField(pathList.getClass(),pathList,"dexElements",allDexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PathClassLoader getPathClassLoader() {
        //自己写的类 默认的类加载器是PathClassLoader
        PathClassLoader pathClassLoader = (PathClassLoader) InsertDexUtils.class.getClassLoader();
        return pathClassLoader;
    }

    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
//        return Reflect.on(paramObject).get("dexElements");
        return FieldUtil.INSTANCE.getFieldValue(paramObject.getClass(), paramObject, "dexElements");
    }

    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
//        return Reflect.on(baseDexClassLoader).get("pathList");
        return FieldUtil.INSTANCE.getFieldValue(BaseDexClassLoader.class, baseDexClassLoader, "pathList");
    }

    private static Object combineArray(Object firstArray, Object secondArray) {
        Class<?> localClass = firstArray.getClass().getComponentType();
        int firstArrayLength = Array.getLength(firstArray);
        int allLength = firstArrayLength + Array.getLength(secondArray);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(firstArray, k));
            } else {
                Array.set(result, k, Array.get(secondArray, k - firstArrayLength));
            }
        }
        return result;
    }


}
