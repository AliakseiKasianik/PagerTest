package clean.domain

import io.reactivex.rxjava3.core.Single

class GetNewsUseCase(private val repository: NewsRepository) {

    operator fun invoke(query: String, page: Int, pageSize: Int): Single<Response> {
        return repository.getNews(query, page, pageSize)
    }
}