package clean.data.repository

import clean.data.mappers.mapToDomain
import clean.data.network.api.NewsApi
import clean.domain.repository.NewsRepository
import clean.domain.model.Response
import io.reactivex.rxjava3.core.Single

class NetworkNewsRepository(private val api: NewsApi) : NewsRepository {

    override fun getNews(query: String, page: Int, pageSize: Int): Single<Response> {
        return api.getNews(query, page, pageSize).map {
           it.mapToDomain()
        }
    }
}