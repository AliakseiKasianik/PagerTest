package koin

import clean.data.repository.NetworkNewsRepository
import clean.domain.repository.NewsRepository
import org.koin.dsl.module

internal val repositoryModule = module {
    single<NewsRepository> { NetworkNewsRepository(api = get()) }
}