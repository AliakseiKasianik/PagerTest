package clean.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import clean.domain.News
import coil.load
import com.itexus.pagertest.R
import com.itexus.pagertest.databinding.ItemNewsBinding

class HomeNewsAdapter(context: Context) :
    PagingDataAdapter<News, HomeNewsViewHolder>(ArticleDiffItemCallback) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNewsViewHolder {
        return HomeNewsViewHolder(layoutInflater.inflate(R.layout.item_news, parent, false))
    }

    override fun onBindViewHolder(holder: HomeNewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HomeNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding by viewBinding(ItemNewsBinding::bind)

    fun bind(news: News?) {
        Log.e("QQQ", news.toString())
        with(viewBinding) {
            image.load(news?.urlToImage) {
                placeholder(ColorDrawable(Color.TRANSPARENT))
            }
            title.text = news?.title
        }
    }
}

private object ArticleDiffItemCallback : DiffUtil.ItemCallback<News>() {

    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.title == newItem.title && oldItem.url == newItem.url
    }
}