package com.xfhy.allinone.scroll.rv

/**
 * @author : xfhy
 * Create time : 2021/2/5 19:35
 * Description :
 */
class NormalItem(val itemData: String) : BaseItem() {

    override val itemType: Int
        get() = NORMAL_ITEM

}