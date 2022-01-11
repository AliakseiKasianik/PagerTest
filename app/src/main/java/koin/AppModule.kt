package koin

import clean.data.NewsRemoteMediator
import org.koin.core.module.Module
import org.koin.dsl.module

internal val app: List<Module> =
    serversModule + activityModule + apiModule + repositoryModule + databaseModule + module {
        single { NewsRemoteMediator(networkRepo = get(), databaseRepo = get()) }
    }