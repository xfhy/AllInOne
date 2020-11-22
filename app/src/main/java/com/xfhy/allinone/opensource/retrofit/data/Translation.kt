package com.xfhy.allinone.opensource.retrofit.data
import com.google.gson.annotations.SerializedName


/**
 * @author : xfhy
 * Create time : 2020/11/22 7:37 AM
 * Description : 金山词霸API 的数据格式
 */
data class Translation(
    @SerializedName("content")
    var content: Content? = Content(),
    @SerializedName("status")
    var status: Int? = 0
)

data class Content(
    @SerializedName("ciba_out")
    var cibaOut: String? = "",
    @SerializedName("ciba_use")
    var cibaUse: String? = "",
    @SerializedName("err_no")
    var errNo: Int? = 0,
    @SerializedName("from")
    var from: String? = "",
    @SerializedName("out")
    var outContent: String? = "",
    @SerializedName("to")
    var to: String? = "",
    @SerializedName("vendor")
    var vendor: String? = ""
)