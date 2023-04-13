package com.xfhy.allinone.jetpack.paging.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xfhy.allinone.jetpack.paging.data.PagingRepository
import com.xfhy.allinone.jetpack.paging.net.GithubRepo
import kotlinx.coroutines.flow.Flow

/**
 * @author : xfhy
 * Create time : 2023/4/13 4:23 下午
 * Description :
 */
class PagingViewModel : ViewModel() {
    fun getPagingData(): Flow<PagingData<GithubRepo>> {
        val pagingData = PagingRepository.getPagingData()
        //将服务器返回的数据在viewModelScope这个作用域内进行缓存.
        // 如配置变更导致Activity重置,则数据还在ViewModel中被保存下来,paging可以直接从里面读缓存数据
        return pagingData.cachedIn(viewModelScope)
    }

}