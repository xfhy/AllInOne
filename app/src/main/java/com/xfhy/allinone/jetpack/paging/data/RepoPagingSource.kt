package com.xfhy.allinone.jetpack.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xfhy.allinone.jetpack.paging.net.GithubRepo
import com.xfhy.allinone.jetpack.paging.net.GithubService

/**
 * @author : xfhy
 * Create time : 2023/4/13 2:54 下午
 * Description :
 */
                                                            //继承PagingSource,第一个参数表示页数的数据类型,第二个参数是每一个item的数据类型
class RepoPagingSource(private val gitHubService: GithubService) : PagingSource<Int, GithubRepo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> {
        return try {
            val page = params.key ?: 1
            //每一页包含多少条数据,可以进行设置
            val pageSize = params.loadSize
            val repoResponse = gitHubService.searchRepos(page, pageSize)
            val repoItems = repoResponse.items
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()) page + 1 else null

            //最后需返回一个LoadResult结果
            //第一个参数: 返回数据列表
            //第二、三个参数:上一页和下一页的页数
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? = null
}