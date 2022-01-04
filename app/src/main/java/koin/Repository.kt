package koin

import clean.data.repository.NewsRepositoryImpl
import clean.domain.NewsRepository
import org.koin.dsl.module

internal val repositoryModule = module {
    single<NewsRepository> { NewsRepositoryImpl(api = get()) }
}