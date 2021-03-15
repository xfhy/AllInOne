package com.xfhy.allinone.image

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.image.big.LoadBigImageActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_image_home.*
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2021/3/15 7:29 AM
 * Description : 图片相关
 */
class ImageHomeActivity : TitleBarActivity() {

    override fun getThisTitle(): CharSequence = "图片相关"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_home)

        btnGoLoadBigImage.setOnClickListener {
            startActivity<LoadBigImageActivity>()
        }
    }

}