package koin

import Const.NEWS_API_KEY
import Const.SERVER_URL
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

internal val serversModule = module {

    single { headerInterceptor() }

    single {
        provideRetrofit(
            client = provideOkHttpClient(interceptor = get()),
            baseUrl = SERVER_URL
        )
    }
}

private fun provideRetrofit(
    client: OkHttpClient,
    baseUrl: String
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(createGsonFactory())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}

private fun createGsonFactory(): GsonConverterFactory {
    val gson = GsonBuilder()
        .setLenient()
        .create()
    return GsonConverterFactory.create(gson)
}

private const val TIMEOUT = 30L
fun provideOkHttpClient(
    interceptor: Interceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .retryOnConnectionFailure(true)
        .build()
}

fun headerInterceptor(): Interceptor =
    Interceptor { chain ->
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("X-Api-Key", NEWS_API_KEY)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }