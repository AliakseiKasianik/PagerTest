package clean.data.network.api

import clean.data.network.model.ResponseDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface NewsApi {

    /**
     * @param query Keywords or phrases to search for in the article title and body
     * @param pageSize The number of results to return per page. Default: 100. Maximum: 100.
     * @param page Use this to page through the results. Default: 1
     * @param queryInBody Keywords or phrases to search for in the article title only
     * @param from A date and optional time for the oldest article allowed. This should be in ISO 8601 format (e.g. 2021-03-11 or 2021-03-11T11:06:13)
     * @param to A date and optional time for the newest article allowed. This should be in ISO 8601 format (e.g. 2021-03-11 or 2021-03-11T11:06:13)
     */
    @GET("/v2/everything")
    fun getNews(
        @Query("q") query: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query("qInTitle") queryInBody: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
    ): Single<ResponseDto>

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}