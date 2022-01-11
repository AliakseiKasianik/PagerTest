package clean.domain.repository

import clean.domain.model.Response
import io.reactivex.rxjava3.core.Single

interface NewsRepository {
    fun getNews(query: String, page: Int, pageSize: Int): Single<Response>
}