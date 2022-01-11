package clean.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import app.AppDatabase
import clean.data.database.api.NewsDao
import clean.data.database.api.RemoteKeysDao
import clean.data.database.model.NewsDb
import clean.data.database.model.RemoteKeys
import clean.data.mappers.mapToNewsDb
import clean.data.repository.NetworkNewsRepository
import clean.domain.model.News
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
                        val remoteKeys = getRemoteKeyForFirstItem(state)/*
                            ?: throw InvalidObjectException("Result is empty")*/
                        Log.e("AAA", remoteKeys.toString() + "prepend")
                        remoteKeys?.prevKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)/*
                            ?: throw InvalidObjectException("Result is empty")*/
                        Log.e("AAA", remoteKeys.toString() + "apend")
                        remoteKeys?.nextKey ?: INVALID_PAGE
                    }
                }
            }.flatMap { page ->
                if (page == INVALID_PAGE) {
                    Log.e("QQQ", page.toString() + " page")
                    Single.just(MediatorResult.Error(InvalidObjectException("cmkjsnkcjdsn-=")))
                } else {
                    networkRepo.getNews("Kotlin", page, state.config.pageSize)
                        .map { response ->
                            Log.e("QQQ", response.toString() + " page")
                            response.listNews.map { it.mapToNewsDb() } }
                        .map { insertToDb(page, loadType, it) }
                        .map<MediatorResult> {
                            Log.e("AAA"," adclsdc " + it.toString())
                            MediatorResult.Success(endOfPaginationReached = it.isEmpty()) }
                        .onErrorReturn {
                            Log.e("AAA", it.toString())
                            MediatorResult.Error(it) }
                }
            }
    }


    private fun insertToDb(page: Int, loadType: LoadType, data: List<NewsDb>): List<NewsDb> {
        Log.e("AAA",  " insert")
        if (loadType == LoadType.REFRESH) {
            Log.e("AAA", loadType.toString() + " loadType")
            remoteKeysDao.clearRemoteKeys()
            newsDao.clearDb()
        }
        val prevKey = if (page == STARTING_PAGE_NUMBER) null else page - 1
        val nextKey = if (data.isEmpty()) null else page + 1
        Log.e("AAA", prevKey.toString() + " prevKey")
        Log.e("AAA", nextKey.toString() + " nextKey")
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
