package com.xfhy.allinone.scroll.rv

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xfhy.allinone.R
import com.xfhy.allinone.scroll.rv.adapter.RvContainsVpAdapter
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_rv_contains_vp.*


/**
 * RecyclerView嵌套ViewPager
 */
class RvContainsVpActivity : TitleBarActivity() {

    override fun getThisTitle() = "RecyclerView嵌套ViewPager"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv_contains_vp)

        //todo xfhy 需要自定义RecyclerView,
        // item滑动到ViewPager并且ViewPager顶部的TabLayout封顶时,将事件交给ViewPager里面的Fragment里面的容器(RecyclerView)
        // 将标题隐藏,

        rvContainsVp.layoutManager = LinearLayoutManager(mContext)
        rvContainsVp.adapter =
            RvContainsVpAdapter(
                getData(),
                supportFragmentManager
            )
        rvContainsVp.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        //判断ViewPager 里面的Fragment里面的RecyclerView是否已经触顶
        // rvContainsVp.setInterceptTouchCallback { false }
    }

    private fun getData(): MutableList<BaseItem> {
        val mutableListOf = mutableListOf<BaseItem>()
        for (i in 1..30) {
            mutableListOf.add(NormalItem("$i"))
        }

        mutableListOf.add(ViewPagerItem())

        return mutableListOf
    }
}
