package clean.data.repository

import clean.data.mapToDomain
import clean.data.network.api.NewsApi
import clean.domain.NewsRepository
import clean.domain.Response
import io.reactivex.rxjava3.core.Single

class NewsRepositoryImpl(private val api: NewsApi) : NewsRepository {

    override fun getNews(query: String, page: Int, pageSize: Int): Single<Response> {
        return api.getNews(query, page, pageSize).map {
           it.mapToDomain()
        }
    }
}