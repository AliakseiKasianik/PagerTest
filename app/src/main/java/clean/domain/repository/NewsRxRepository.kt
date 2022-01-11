package clean.domain.repository

import androidx.paging.PagingData
import clean.data.database.model.NewsDb
import io.reactivex.rxjava3.core.Flowable

interface NewsRxRepository {
    fun getNews(): Flowable<PagingData<NewsDb>>
}