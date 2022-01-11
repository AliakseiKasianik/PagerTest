package clean.presentation.screen

import androidx.paging.PagingData
import androidx.paging.rxjava3.mapAsync
import clean.data.database.model.NewsDb
import clean.domain.repository.NewsRxRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class MainActivityViewModel(
    repository: NewsRxRepository
) : BaseMainActivityViewModel() {

    override val news: Flowable<PagingData<NewsDb>> = repository.getNews()
        .map { pagingData -> pagingData.mapAsync { Single.just(it) } }
}


