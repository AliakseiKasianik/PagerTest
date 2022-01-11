package clean.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import clean.data.database.model.NewsDb
import clean.domain.model.News
import com.itexus.pagertest.databinding.ItemNewsBinding

class HomeNewsAdapter :
    PagingDataAdapter<NewsDb, NewsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
        ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<NewsDb>() {

            override fun areItemsTheSame(oldItem: NewsDb, newItem: NewsDb): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NewsDb, newItem: NewsDb): Boolean {
                return oldItem.title == newItem.title && oldItem.url == newItem.url
            }
        }
    }
}