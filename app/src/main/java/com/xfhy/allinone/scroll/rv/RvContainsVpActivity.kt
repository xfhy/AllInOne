package com.xfhy.allinone.scroll.rv

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xfhy.allinone.R
import com.xfhy.allinone.scroll.rv.adapter.RvContainsVpAdapter
import com.xfhy.allinone.scroll.rv.view.ChildRecyclerView
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_rv_contains_vp.*


/**
 * RecyclerView嵌套ViewPager
 * github开源方案:  https://github.com/JasonGaoH/NestedRecyclerView
 */
class RvContainsVpActivity : TitleBarActivity() {

    override fun getThisTitle() = "RecyclerView嵌套ViewPager"

    private val mRvContainsVpAdapter by lazy {
        RvContainsVpAdapter(
            getData(),
            supportFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv_contains_vp)

        //todo xfhy 需要自定义RecyclerView,
        // item滑动到ViewPager并且ViewPager顶部的TabLayout封顶时,将事件交给ViewPager里面的Fragment里面的容器(RecyclerView)
        // 将标题隐藏,

        rvContainsVp.layoutManager =
            object : LinearLayoutManager(mContext, VERTICAL, false) {

                /**
                 * 竖直方向上是否可以滑动
                 */
                override fun canScrollVertically(): Boolean {
                    //找到当前的ChildRecyclerView
                    val childRecyclerView = findChildRecyclerView()

                    log(
                        com.xfhy.allinone.scroll.rv.view.TAG,
                        "childRecyclerView = $childRecyclerView   childRecyclerView?.isScrollTop() = ${childRecyclerView?.isScrollTop()}"
                    )

                    //只有当前ChildRecyclerView滑动到顶部,才认为ParentRecyclerView是可以竖直方向是可以滚动的
                    return childRecyclerView?.isScrollTop() ?: true
                }
            }

        rvContainsVp.mGetChildCallback = {
            findChildRecyclerView()
        }
        rvContainsVp.adapter = mRvContainsVpAdapter

        rvContainsVp.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun findChildRecyclerView(): ChildRecyclerView? {
        return mRvContainsVpAdapter.data?.let { dataList ->
            if (dataList.size > 0) {
                val lastItem = dataList[dataList.size - 1]
                if (lastItem is ViewPagerItem) {
                    return lastItem.findChildRecyclerView()
                }
            }
            null
        }
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
