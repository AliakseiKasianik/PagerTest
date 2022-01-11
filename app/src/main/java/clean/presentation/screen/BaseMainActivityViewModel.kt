package clean.presentation.screen

import androidx.paging.PagingData
import clean.domain.model.News
import io.reactivex.rxjava3.core.Flowable
import utils.RxViewModel

abstract class BaseMainActivityViewModel: RxViewModel() {

    abstract val news: Flowable<PagingData<News>>
}