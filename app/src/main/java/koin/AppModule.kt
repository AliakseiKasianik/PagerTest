package koin

import org.koin.core.module.Module

internal val app: List<Module> =
    serversModule + activityModule + apiModule + repositoryModule + useCase