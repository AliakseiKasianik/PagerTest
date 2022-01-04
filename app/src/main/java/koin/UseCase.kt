package koin

import clean.domain.GetNewsUseCase
import org.koin.dsl.module

internal val useCase = module {
    factory { GetNewsUseCase(repository = get()) }
}