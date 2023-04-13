package com.xfhy.allinone.jetpack.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xfhy.allinone.R
import com.xfhy.allinone.jetpack.paging.net.GithubRepo

/**
 * @author : xfhy
 * Create time : 2023/4/13 4:28 下午
 * Description :
 */
//1. 必须继承自PagingDataAdapter
//2. 必须提供一个COMPARATOR,paging内部会使用DiffUtil来管理数据变化
//3. 不需要传递什么数据源,也不需要重写getItemCount方法,paging在内部已经管理好了
class GithubRepoAdapter : PagingDataAdapter<GithubRepo, GithubRepoAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<GithubRepo>() {
            override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.github_repo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getItem是PagingDataAdapter里面的一个方法,可以获取到item数据
        val repo = getItem(position)
        holder.name.text = repo?.name
        holder.description.text = repo?.description
        holder.starCount.text = repo?.starCount.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_text)
        val description: TextView = itemView.findViewById(R.id.description_text)
        val starCount: TextView = itemView.findViewById(R.id.star_count_text)
    }

}