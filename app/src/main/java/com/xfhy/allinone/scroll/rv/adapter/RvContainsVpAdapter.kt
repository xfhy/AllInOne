package com.xfhy.allinone.scroll.rv.adapter

import androidx.fragment.app.FragmentManager
import com.xfhy.allinone.R
import com.xfhy.allinone.scroll.rv.*
import com.xfhy.library.adapter.BaseMultiItemQuickAdapter
import com.xfhy.library.adapter.BaseViewHolder

/**
 * @author : xfhy
 * Create time : 2021/2/5 19:37
 * Description :
 */
class RvContainsVpAdapter(
    data: MutableList<BaseItem>,
    val supportFragmentManager: FragmentManager
) :
    BaseMultiItemQuickAdapter<BaseItem, BaseViewHolder>(data) {

    init {
        addItemType(NORMAL_ITEM, R.layout.rv_contains_vp_normal_item)
        addItemType(VIEWPAGER_ITEM, R.layout.rv_contains_vp_item)
    }

    override fun convert(holder: BaseViewHolder, item: BaseItem?) {
        if (item == null) {
            return
        }
        when (holder.itemViewType) {
            NORMAL_ITEM -> {
                bindNormalItem(holder, item)
            }
            VIEWPAGER_ITEM -> {
                bindVpItem(holder, item)
            }
            else -> {
            }
        }
    }

    private fun bindVpItem(holder: BaseViewHolder, item: BaseItem) {
        if (item !is ViewPagerItem) {
            return
        }
        item.bindItem(holder, supportFragmentManager)
    }

    private fun bindNormalItem(holder: BaseViewHolder, item: BaseItem) {
        if (item !is NormalItem) {
            return
        }
        holder.setText(R.id.tv_normal_item, item.itemData)
    }
}