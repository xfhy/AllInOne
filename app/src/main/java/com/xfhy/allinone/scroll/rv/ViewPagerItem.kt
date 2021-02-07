package com.xfhy.allinone.scroll.rv

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.xfhy.allinone.R
import com.xfhy.allinone.scroll.rv.adapter.InnerVpPagerAdapter
import com.xfhy.library.adapter.BaseViewHolder

/**
 * @author : xfhy
 * Create time : 2021/2/5 19:35
 * Description :
 */
class ViewPagerItem : BaseItem() {

    override val itemType: Int
        get() = VIEWPAGER_ITEM

    //-------------------测试数据----------------------------
    private val fragmentList = mutableListOf<Fragment>()
    private var isSetupWithViewPager = false

    init {
        fragmentList.clear()
        fragmentList.add(VpNormalFragment())
        fragmentList.add(VpNormalFragment())
        fragmentList.add(VpNormalFragment())
        fragmentList.add(VpNormalFragment())
    }

    fun bindItem(
        holder: BaseViewHolder,
        supportFragmentManager: FragmentManager
    ) {
        val tabLayout = holder.getView(R.id.vp_item_tab_layout) as TabLayout
        val viewPager = holder.getView(R.id.vp_item_vp) as ViewPager

        if (!isSetupWithViewPager) {
            tabLayout.setupWithViewPager(viewPager)
            isSetupWithViewPager = true
        }
        if (viewPager.adapter == null) {
            viewPager.adapter = InnerVpPagerAdapter(fragmentList, supportFragmentManager)
        }
    }

}