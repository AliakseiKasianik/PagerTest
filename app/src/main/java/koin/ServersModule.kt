package koin

import Const.API_KEY_HEADER
import Const.NEWS_API_KEY
import Const.NEWS_API_KEY_SECOND
import Const.SERVER_URL
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal val serversModule = module {

    single(InterceptorQualifier.header) { headerKeyInterceptor() }
    single(InterceptorQualifier.logging) { provideLoggingInterceptor() }

    single {
        provideRetrofit(
            client = provideOkHttpClient(
                interceptors = listOf(
                    get(InterceptorQualifier.header),
                    get(InterceptorQualifier.logging)
                )
            ),
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
    interceptors: List<Interceptor>? = null
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .apply { interceptors?.forEach { addInterceptor(it) } }
        .build()
}

fun headerKeyInterceptor(): Interceptor =
    Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader(API_KEY_HEADER, NEWS_API_KEY_SECOND)
            .build()
        chain.proceed(newRequest)
    }

//Not working http logger
private fun provideLoggingInterceptor(): Interceptor {
    val logger = HttpLoggingInterceptor.Logger { message -> Timber.d("OkHttp -> $message") }
    return HttpLoggingInterceptor(logger)
        .apply { level = HttpLoggingInterceptor.Level.BODY }
}

object InterceptorQualifier {
    val logging = StringQualifier("logging")
    val header = StringQualifier("header")
}
