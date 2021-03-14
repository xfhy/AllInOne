package com.xfhy.allinone.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_load_big_image.*

/**
 * @author : xfhy
 * Create time : 2021/3/15 7:29 AM
 * Description : 加载巨图
 * 参考方案: https://blog.csdn.net/lmj623565791/article/details/49300989
 */
class LoadBigImageActivity : TitleBarActivity() {

    override fun getThisTitle(): CharSequence = "加载巨图"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_big_image)

        btnLoadImage.setOnClickListener {
            showBigImage()
        }
    }

    private fun showBigImage() {
        val bitmapRegionDecoder = BitmapRegionDecoder.newInstance(assets.open("tobias.jpg"), false)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = bitmapRegionDecoder.decodeRegion(Rect(0, 0, 200, 200), options)
        ivShowImage.setImageBitmap(bitmap)
    }

}