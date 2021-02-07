package com.xfhy.allinone.scroll.rv

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xfhy.allinone.R
import com.xfhy.library.adapter.BaseQuickAdapter
import com.xfhy.library.adapter.BaseViewHolder
import com.xfhy.library.basekit.fragment.BaseFragment
import java.util.*

/**
 * @author : xfhy
 * Create time : 2021/2/5 20:13
 * Description :
 */
class VpNormalFragment : BaseFragment() {

    override fun getLayoutResId(): Int = R.layout.vp_normal_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mRootView is RecyclerView) {
            val recyclerView = mRootView as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = InnerAdapter(getTestData())
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    private fun getTestData(): MutableList<InnerItemData> {
        val resultList = mutableListOf<InnerItemData>()
        for (i in 0..40) {
            resultList.add(InnerItemData(("$i--"), getTestBgColor(Random().nextInt(10))))
        }

        return resultList
    }

    private fun getTestBgColor(randomNumber: Int) = when (randomNumber) {
        0, 4, 8 -> {
            Color.parseColor("#7B68EE")
        }
        1, 5, 9 -> {
            Color.parseColor("#8A2BE2")
        }
        2, 6 -> {
            Color.parseColor("#1E90FF")
        }
        3, 7 -> {
            Color.parseColor("#008000")
        }
        else -> {
            Color.parseColor("#BA55D3")
        }
    }

    data class InnerItemData(val text: String, val bgColor: Int)

    class InnerAdapter(data: MutableList<InnerItemData>) :
        BaseQuickAdapter<InnerItemData, BaseViewHolder>(R.layout.item_rv_contains_vp_normal, data) {

        override fun convert(holder: BaseViewHolder, item: InnerItemData?) {
            holder.setText(R.id.tv_normal_item, item?.text)
            holder.setBackgroundColor(R.id.tv_normal_item, item?.bgColor ?: 0xffffffff.toInt())
        }
    }

}