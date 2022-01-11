package clean.presentation.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import clean.domain.model.News
import coil.load
import com.itexus.pagertest.databinding.ItemNewsBinding

class NewsViewHolder(private val binding: ItemNewsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News?) {
        with(binding) {
            image.load(news?.urlToImage) {
                placeholder(ColorDrawable(Color.TRANSPARENT))
            }
            title.text = news?.title
        }
    }
}