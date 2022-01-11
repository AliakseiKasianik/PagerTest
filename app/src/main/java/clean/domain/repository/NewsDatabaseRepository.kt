package clean.domain.repository

import androidx.paging.PagingSource
import clean.data.database.model.NewsDb
import io.reactivex.rxjava3.core.Completable

interface NewsDatabaseRepository {
    fun insertNews(news: List<NewsDb>): Completable
    fun clearDb(): Completable
    fun getNews(): PagingSource<Int, NewsDb>
}