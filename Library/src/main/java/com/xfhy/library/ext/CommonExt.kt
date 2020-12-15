package com.xfhy.library.ext

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.xfhy.library.utils.SnackbarUtil
import com.xfhy.library.widgets.DefaultTextWatcher

/**
 * @author xfhy
 * time create at 2018/1/28 9:26
 * description 通用的扩展
 */

/**
 * Button 扩展  取决于EditText是否有值
 */
fun Button.enable(et: EditText, method: () -> Boolean) {
    val btn = this
    et.addTextChangedListener(object : DefaultTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btn.isEnabled = method()
        }
    })
}

//Activity中展示snackbar
fun Activity.snackbar(
    view: View,
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    type: Int = SnackbarUtil.INFO
) {
    SnackbarUtil.showCustomTimeSnackbar(view, message, Snackbar.LENGTH_SHORT, SnackbarUtil.INFO)
}

fun Fragment.toast(str: String) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Any?.logString(tag: String = "xfhy_tag") {
    Log.d(tag, this.toString())
}

fun log(tag: String = "xfhy_tag", msg: String?) {
    Log.d(tag, msg ?: "null")
}

fun log(msg: String?) {
    Log.d("xfhy_tag", msg ?: "null")
}