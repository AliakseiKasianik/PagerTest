package clean.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import app.AppDatabase
import clean.data.database.model.NewsDb
import clean.data.database.model.RemoteKeys
import clean.data.mappers.mapToNewsDb
import clean.data.repository.NetworkNewsRepository
import clean.domain.model.News
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val networkRepo: NetworkNewsRepository,
    private val databaseRepo: AppDatabase
) : RxRemoteMediator<Int, News>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, News>
    ): Single<MediatorResult> {

        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when (it) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_NUMBER
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                            ?: throw InvalidObjectException("Result is empty")
                        remoteKeys.prevKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                            ?: throw InvalidObjectException("Result is empty")
                        remoteKeys.nextKey ?: INVALID_PAGE
                    }
                }
            }.flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    networkRepo.getNews("Kotlin", page, state.config.pageSize)
                        .map { response -> response.listNews.map { it.mapToNewsDb() } }
                        .map { insertToDb(page, loadType, it) }
                        .map<MediatorResult> { MediatorResult.Success(endOfPaginationReached = it.isEmpty()) }
                        .onErrorReturn { MediatorResult.Error(it) }
                }
            }
    }


    private fun insertToDb(page: Int, loadType: LoadType, data: List<NewsDb>): List<NewsDb> {
        if (loadType == LoadType.REFRESH) {
            databaseRepo.remoteKeysDao().clearRemoteKeys()
            databaseRepo.newsDao().clearDb()
        }
        val prevKey = if (page == STARTING_PAGE_NUMBER) null else page - 1
        val nextKey = if (data.isEmpty()) null else page + 1
        val keys = data.map {
            RemoteKeys(repoId = it.newsId, prevKey = prevKey, nextKey = nextKey)
        }
        databaseRepo.remoteKeysDao().insertAll(keys)
        databaseRepo.newsDao().insertNews(data)
        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, News>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                databaseRepo.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, News>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                databaseRepo.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, News>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                databaseRepo.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }

    companion object {
        const val STARTING_PAGE_NUMBER = 1
        const val INVALID_PAGE = -1
    }
}
