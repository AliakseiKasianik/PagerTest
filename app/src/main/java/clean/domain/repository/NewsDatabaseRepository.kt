package clean.domain.repository

import androidx.paging.PagingSource
import clean.data.database.model.NewsDb

interface NewsDatabaseRepository {
    fun insertNews(news: List<NewsDb>)
    fun clearDb()
    fun getNews(): PagingSource<Int, NewsDb>
}