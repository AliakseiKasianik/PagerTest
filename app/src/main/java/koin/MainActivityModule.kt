package koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import clean.presentation.screen.BaseMainActivityViewModel
import clean.presentation.screen.MainActivityViewModel

internal val activityModule = module {

    viewModel<BaseMainActivityViewModel> {
        MainActivityViewModel(
            databaseRepo = get(),
            newsRemoteMediator = get()
        )
    }
}