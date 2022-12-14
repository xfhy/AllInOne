package com.xfhy.allinone.base

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginTop
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.dp
import kotlinx.android.synthetic.main.activity_base_demo.*

/**
 * @author : xfhy
 * Create time : 2022/12/15 6:59
 * Description : demo通用Activity，只需往里面添加button就行
 */
abstract class BaseDemoActivity : TitleBarActivity(), View.OnClickListener {

    private val onClickMethodMap = mutableMapOf<Int, (() -> Unit)>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_demo)
        initButtons()
    }

    abstract fun initButtons()

    protected fun addButtonItem(buttonText: String, onClickMethod: () -> Unit) {
        val button = Button(this, null, 0, R.style.HorizontalBtnStyle).apply {
            id = View.generateViewId()
            onClickMethodMap[id] = onClickMethod
            text = buttonText
            setOnClickListener(this@BaseDemoActivity)
        }
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 15.dp
        ll_business.addView(button, layoutParams)
    }

    override fun onClick(v: View?) {
        v ?: return
        onClickMethodMap[v.id]?.invoke()
    }

}