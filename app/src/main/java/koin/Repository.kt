package koin

import app.AppDatabase
import clean.data.repository.NetworkNewsRepository
import clean.data.repository.NewsRxRepositoryImpl
import clean.domain.repository.NewsRepository
import clean.domain.repository.NewsRxRepository
import org.koin.dsl.module

internal val repositoryModule = module {
    single<NewsRepository> { NetworkNewsRepository(api = get()) }
    single<NewsRxRepository> {
        NewsRxRepositoryImpl(
            newsDao = get<AppDatabase>().newsDao(),
            remoteKeysDao = get<AppDatabase>().remoteKeysDao(),
            networkRepo = get()
        )
    }
}