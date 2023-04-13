package com.xfhy.allinone.jetpack

import com.xfhy.allinone.base.BaseDemoActivity
import com.xfhy.allinone.jetpack.paging.PagingActivity
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2023/04/13 14:28
 * Description : Jetpack
 */
class JetpackMainActivity : BaseDemoActivity() {

    override fun getThisTitle(): CharSequence {
        return "Jetpack"
    }

    override fun initButtons() {
        addButtonItem("Paging", ::goPagingPage)
    }

    private fun goPagingPage() {
        startActivity<PagingActivity>()
    }

}