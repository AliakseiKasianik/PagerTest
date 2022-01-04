package utils

import retrofit2.Retrofit

inline fun <reified Api> provideApi(retrofit: Retrofit): Api {
    return retrofit.create(Api::class.java)
}