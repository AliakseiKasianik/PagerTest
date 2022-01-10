package clean.data.network

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import clean.domain.GetNewsUseCase
import clean.domain.News
import clean.domain.Response
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsPagingSource(private val getNewsUseCase: GetNewsUseCase) : RxPagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, News>> {

        val pageNumber = params.key ?: 1
        val pageSize = params.loadSize

        return getNewsUseCase("Kotlin", pageNumber, pageSize)
            .subscribeOn(Schedulers.io())
            .map { toResult(it, pageNumber) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toResult(
        response: Response,
        pageNumber: Int
    ): LoadResult<Int, News> {
        val news = response.listNews
        val nextPageNumber = if (news.isEmpty()) null else pageNumber + 1
        val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null

        return LoadResult.Page(
            data = response.listNews,
            prevKey = prevPageNumber,
            nextKey = nextPageNumber
        )
    }
}