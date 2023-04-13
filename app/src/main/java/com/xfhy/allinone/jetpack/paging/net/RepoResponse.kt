package com.xfhy.allinone.jetpack.paging.net

import com.google.gson.annotations.SerializedName

/**
 * @author : xfhy
 * Create time : 2023/4/13 2:50 下午
 * Description :
 */
class RepoResponse(
    @SerializedName("items") val items: List<GithubRepo> = emptyList()
)