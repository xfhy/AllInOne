package com.xfhy.allinone.scroll.rv

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.xfhy.allinone.R
import com.xfhy.library.adapter.BaseViewHolder

/**
 * @author : xfhy
 * Create time : 2021/2/5 19:35
 * Description :
 */
class ViewPagerItem : BaseItem() {

    override val itemType: Int
        get() = VIEWPAGER_ITEM

    val fragmentList = mutableListOf<Fragment>()

    fun bindItem(
        holder: BaseViewHolder,
        supportFragmentManager: FragmentManager
    ) {

        fragmentList.add(VpNormalFragment())
        fragmentList.add(VpNormalFragment())
        fragmentList.add(VpNormalFragment())
        fragmentList.add(VpNormalFragment())

        val tabLayout = holder.getView(R.id.vp_item_tab_layout) as TabLayout
        val viewPager = holder.getView(R.id.vp_item_vp) as ViewPager

        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment = fragmentList[position]

            override fun getCount(): Int = fragmentList.size
        }

    }


}