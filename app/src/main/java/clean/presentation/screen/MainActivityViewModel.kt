package clean.presentation.screen

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import androidx.paging.rxjava3.mapAsync
import clean.data.database.model.NewsDb
import clean.data.database.model.SourceDb
import clean.data.network.NewsPagingSource
import clean.data.network.model.SourceDto
import clean.domain.GetNewsUseCase
import clean.domain.News
import clean.domain.NewsDatabaseRepository
import clean.domain.Source
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivityViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val databaseRepo: NewsDatabaseRepository
) : BaseMainActivityViewModel() {

    @ExperimentalCoroutinesApi
    override val news: Flowable<PagingData<News>> =
        Pager(PagingConfig(pageSize = 20, enablePlaceholders = false), pagingSourceFactory = {
            NewsPagingSource(getNewsUseCase)
        }).flowable.map { pagingData -> pagingData.mapAsync { Single.just(it) } }

    init {

        getNewsUseCase("Kotlin", 1, 60)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable {
                Log.e("AAA", it.listNews.toString())
                databaseRepo.insertNews(
                    it.listNews.mapToDb()
                ).doOnComplete {
                    Log.e("AAA", " complete")

                }
                    .doOnError {
                        Log.e("AAA", it.toString() + " error")
                    }
            }.subscribeByViewModel()

        databaseRepo.getNews()
            .subscribeOn(Schedulers.io())
            .doOnNext {
                Log.e("SSS", it.toString())
            }.doOnError {
                Log.e("SSS", it.toString())
            }.subscribeByViewModel()
    }


    private fun List<News>.mapToDb(): List<NewsDb> {
        return this.map {
            NewsDb(
                source = it.source?.mapToSourceDb(),
                title = it.title,
                url = it.url,
                description = it.description,
                author = it.author,
                urlToImage = it.urlToImage,
                publishedAt = it.publishedAt,
                content = it.content
            )
        }
    }

    private fun Source.mapToSourceDb(): SourceDb {
        return SourceDb(
            id = id,
            name = name
        )
    }
}


