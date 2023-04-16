package com.xfhy.allinone.jetpack.paging.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xfhy.allinone.jetpack.paging.net.GithubRepo
import com.xfhy.allinone.jetpack.paging.net.GithubService
import kotlinx.coroutines.flow.Flow

/**
 * @author : xfhy
 * Create time : 2023/4/13 4:18 下午
 * Description :
 */
object PagingRepository {

    private const val PAGE_SIZE = 50

    private val githubService = GithubService.create()

    //除GithubRepo外,其他都是固定写法
    fun getPagingData(): Flow<PagingData<GithubRepo>> {
        //创建Pager对象,调用.flow将它转换成一个Flow对象
        return Pager(
            //PagingConfig用于指定一些参数：提前多久加载、初始加载请求的大小、页面个数等
            //指定每页包含的数据量
            config = PagingConfig(PAGE_SIZE),
            //将自定义的RepoPagingSource传入,paging会用它来作为用于分页的数据源
            pagingSourceFactory = { RepoPagingSource(githubService) }
        ).flow
    }

}