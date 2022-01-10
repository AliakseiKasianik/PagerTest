package clean.data.repository

import clean.data.database.api.NewsDao
import clean.data.database.model.NewsDb
import clean.domain.NewsDatabaseRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

data class RoomNewsRepository(private val dao: NewsDao): NewsDatabaseRepository {
    override fun insertNews(news: List<NewsDb>): Completable {
        return dao.insertNews(news)
    }

    override fun clearDb(): Completable {
        return dao.clearDb()
    }

    override fun getNews(): Observable<List<NewsDb>> {
        return dao.getNews()
    }
}