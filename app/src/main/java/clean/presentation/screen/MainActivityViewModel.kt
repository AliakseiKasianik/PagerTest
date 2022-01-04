package clean.presentation.screen

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import androidx.paging.rxjava3.mapAsync
import clean.data.network.NewsPagingSource
import clean.domain.GetNewsUseCase
import clean.domain.News
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class MainActivityViewModel(
    private val getNewsUseCase: GetNewsUseCase
) : BaseMainActivityViewModel() {

    @ExperimentalCoroutinesApi
    override val news: Flowable<PagingData<News>> =
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = {
            NewsPagingSource(getNewsUseCase)
        }).flowable.map { pagingData -> pagingData.mapAsync { Single.just(it) } }
}


