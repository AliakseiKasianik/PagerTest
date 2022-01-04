package koin

import clean.data.network.api.NewsApi
import org.koin.dsl.module
import utils.provideApi

internal val apiModule = module {
    single<NewsApi> {
        provideApi(retrofit = get())
    }
}