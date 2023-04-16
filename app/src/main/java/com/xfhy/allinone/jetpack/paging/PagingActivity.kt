package com.xfhy.allinone.jetpack.paging

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xfhy.allinone.R
import com.xfhy.allinone.jetpack.paging.viewmodel.PagingViewModel
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.coroutines.launch

/**
 * @author : xfhy
 * Create time : 2023/04/13 14:28
 * Description : Paging
 *
 * GitHub api : https://api.github.com/search/repositories?sort=stars&q=Android&per_page=5&page=1
 *
 * 用了paging之后,它会自动去加载数据.paging将细节隐藏到了它内部,开发者无需监听滑动事件,无需在列表底部手动去加载数据.按他的模子来就行.
 *
 * paging3在列表远没有滑到底部时就提前加载更多数据(默认属性,可配置)
 *
 * todo xfhy 下面的需求，怎么实现：
 * 1. 下拉刷新
 * 2. header
 * 3. 在某些位置上增加item（类似，增加banner广告，不是从github api取的）
 * 4. 多种type的item，怎么处理
 * 5. 有哪些局限性
 * 6. 用户一直往后面滑动，内存里面的数据（list data）不断增多，paging是如何解决这个问题的？
 * 官方paging教程 https://developer.android.com/codelabs/android-paging?hl=zh-cn#0
 * 官方kotlin flow教程 https://developer.android.com/codelabs/advanced-kotlin-coroutines?hl=zh-cn#0
 */
class PagingActivity : TitleBarActivity() {

    //第一种写法
//    private val viewModel by lazy {
//        ViewModelProvider(this)[PagingViewModel::class.java]
//    }

    //第二种写法
    //viewModels函数实际上是一个扩展函数.  activity-ktx里面的
    private val viewModel by viewModels<PagingViewModel>()

    private val repoAdapter = GithubRepoAdapter()

    override fun getThisTitle(): CharSequence {
        return "Paging"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //简单实用
//        recyclerView.adapter = repoAdapter
        //配合footer使用
        recyclerView.adapter = repoAdapter.withLoadStateFooter(FooterAdapter { repoAdapter.retry() })

        lifecycleScope.launch {
            //获取数据,然后往repoAdapter里面放
            //这里调用collect之后,消息才能源源不断地往这里传
            viewModel.getPagingData().collect { pagingData ->
                // pagingData: PagingData<GithubRepo>
                repoAdapter.submitData(pagingData)
            }
        }

        //load状态监听
        repoAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Load Error: ${state.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}