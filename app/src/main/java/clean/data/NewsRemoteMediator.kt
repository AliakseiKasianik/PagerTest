package clean.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import clean.data.database.api.NewsDao
import clean.data.database.api.RemoteKeysDao
import clean.data.database.model.NewsDb
import clean.data.database.model.RemoteKeys
import clean.data.mappers.mapToNewsDb
import clean.domain.repository.NewsRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val networkRepo: NewsRepository,
    private val newsDao: NewsDao,
    private val remoteKeysDao: RemoteKeysDao
) : RxRemoteMediator<Int, NewsDb>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, NewsDb>
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
                        remoteKeys?.prevKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                        remoteKeys?.nextKey ?: INVALID_PAGE
                    }
                }
            }.flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Error(InvalidObjectException("Invalid page")))
                } else {
                    networkRepo.getNews("Kotlin", page, state.config.pageSize)
                        .map { response ->
                            insertToDb(
                                page,
                                loadType,
                                response.listNews.map { it.mapToNewsDb() }
                            )
                        }
                        .map<MediatorResult> {
                            MediatorResult.Success(endOfPaginationReached = it.isEmpty())
                        }
                        .onErrorReturn {
                            MediatorResult.Error(it)
                        }
                }
            }
    }


    private fun insertToDb(
        page: Int,
        loadType: LoadType,
        data: List<NewsDb>
    ): List<NewsDb> {
        if (loadType == LoadType.REFRESH) {
            remoteKeysDao.clearRemoteKeys()
            newsDao.clearDb()
        }

        val prevKey = if (page > STARTING_PAGE_NUMBER) page - 1 else null
        val nextKey = if (data.isEmpty()) null else page + 1

        val keys = data.map {
            RemoteKeys(repoId = it.newsId, prevKey = prevKey, nextKey = nextKey)
        }
        remoteKeysDao.insertAll(keys)
        newsDao.insertNews(data)
        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, NewsDb>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                remoteKeysDao.remoteKeysRepoId(repo.newsId)
            }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, NewsDb>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                remoteKeysDao.remoteKeysRepoId(repo.newsId)
            }
    }

    private fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, NewsDb>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.newsId?.let { repoId ->
                remoteKeysDao.remoteKeysRepoId(repoId)
            }
        }
    }

    companion object {
        const val STARTING_PAGE_NUMBER = 1
        const val INVALID_PAGE = -1
    }
}
