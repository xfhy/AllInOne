package com.xfhy.allinone.jetpack.paging.net

import com.google.gson.annotations.SerializedName

/**
 * @author : xfhy
 * Create time : 2023/4/13 2:50 下午
 * Description :
 */
data class GithubRepo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("stargazers_count") val starCount: Int
)
