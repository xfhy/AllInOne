package com.xfhy.allinone.scroll.rv.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author : xfhy
 * Create time : 2021/2/7 10:26
 * Description : 内部ViewPager的adapter
 */
class InnerVpPagerAdapter(
    val fragmentList: MutableList<Fragment>,
    supportFragmentManager: FragmentManager
) : FragmentStatePagerAdapter(
    supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val tabTitles = mutableListOf<String>()

    init {
        (0..fragmentList.size).forEach { i ->
            tabTitles.add("Tab$i")
        }
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

}