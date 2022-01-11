package clean.presentation.screen

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import androidx.paging.rxjava3.mapAsync
import clean.data.NewsRemoteMediator
import clean.domain.model.News
import clean.domain.repository.NewsDatabaseRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class MainActivityViewModel(
    private val databaseRepo: NewsDatabaseRepository,
    newsRemoteMediator: NewsRemoteMediator
) : BaseMainActivityViewModel() {

    @ExperimentalPagingApi
    override val news: Flowable<PagingData<News>> =
        Pager(PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = newsRemoteMediator,
            pagingSourceFactory = { databaseRepo.getNews() }
        ).flowable.map { pagingData -> pagingData.mapAsync { Single.just(it) } }
}


