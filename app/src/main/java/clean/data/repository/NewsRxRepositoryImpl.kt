package clean.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import clean.data.NewsRemoteMediator
import clean.data.database.api.NewsDao
import clean.data.database.api.RemoteKeysDao
import clean.data.database.model.NewsDb
import clean.domain.repository.NewsRepository
import clean.domain.repository.NewsRxRepository
import io.reactivex.rxjava3.core.Flowable

class NewsRxRepositoryImpl(
    private val newsDao: NewsDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val networkRepo: NewsRepository
) : NewsRxRepository {

    @ExperimentalPagingApi
    override fun getNews(): Flowable<PagingData<NewsDb>> {
        val newsMediator = NewsRemoteMediator(networkRepo, newsDao, remoteKeysDao)
        return Pager(PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = newsMediator,
            pagingSourceFactory = {
                newsDao.getNews()
            }).flowable
    }
}