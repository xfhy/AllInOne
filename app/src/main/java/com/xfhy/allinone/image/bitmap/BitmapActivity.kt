package com.xfhy.allinone.image.bitmap

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2021/3/18
 * Description : Bitmap
 * 知识点:
 * Bitmap.getAllocationByteCount()方法获取bitmap占用的字节大小
 * Bitmap.Config.ARGB_8888方式存储,每个像素占4个字节
 * 通过 BitmapFactory.Options 的 inPreferredConfig 选项，将存储方式设置为Bitmap.Config.RGB_565,每个像素占2个字节
 * Options中还有一个 inSampleSize 参数，可以实现 Bitmap 采样压缩，这个参数的含义是宽高维度上每隔 inSampleSize 个像素进行一次采集
 * 内存中已经存在了一个 Bitmap 对象。每次切换图片只是显示的内容不一样，我们可以重复利用已经占用内存的 Bitmap 空间，具体做法就是使用 Options.inBitmap 参数
 * BitmapRegionDecoder 图片分片显示,加载长图时的部分浏览
 * Bitmap 缓存,LruCache
 */
class BitmapActivity : TitleBarActivity() {

    override fun getThisTitle(): CharSequence = "Bitmap"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)
    }

}