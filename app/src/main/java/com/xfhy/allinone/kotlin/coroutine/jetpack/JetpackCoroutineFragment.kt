package com.xfhy.allinone.kotlin.coroutine.jetpack

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.fragment.BaseFragment
import kotlinx.coroutines.launch

/**
 * @author : xfhy
 * Create time : 2021/5/30 3:54 下午
 * Description :
 */
class JetpackCoroutineFragment:BaseFragment() {

    override fun getLayoutResId() = R.layout.fragment_jetpack_coroutine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {

        }
    }
}