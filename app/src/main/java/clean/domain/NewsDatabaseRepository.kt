package clean.domain

import androidx.paging.PagingSource
import androidx.paging.rxjava3.RxPagingSource
import clean.data.database.model.NewsDb
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface NewsDatabaseRepository {
    fun insertNews(news: List<NewsDb>): Completable
    fun clearDb(): Completable
    fun getNews(): Observable<List<NewsDb>>
}