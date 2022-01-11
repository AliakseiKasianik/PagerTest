package clean.data.repository

import androidx.paging.PagingSource
import clean.data.database.api.NewsDao
import clean.data.database.model.NewsDb
import clean.domain.repository.NewsDatabaseRepository
import io.reactivex.rxjava3.core.Completable

data class RoomNewsRepository(private val dao: NewsDao): NewsDatabaseRepository {
    override fun insertNews(news: List<NewsDb>): Completable {
        return dao.insertNews(news)
    }

    override fun clearDb(): Completable {
        return dao.clearDb()
    }

    override fun getNews(): PagingSource<Int, NewsDb> {
        return dao.getNews()
    }
}