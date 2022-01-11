package clean.presentation.screen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import clean.presentation.adapter.HomeNewsAdapter
import clean.presentation.adapter.NewsLoadStateAdapter
import com.itexus.pagertest.R
import com.itexus.pagertest.databinding.MainActivityLayoutBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import utils.subscribe

class MainActivity : AppCompatActivity(R.layout.main_activity_layout) {

    private val binding by viewBinding(MainActivityLayoutBinding::bind)
    private val viewModel by viewModel<BaseMainActivityViewModel>()

    private val adapter = HomeNewsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            news.adapter = adapter
            news.setHasFixedSize(true)

            adapter.addLoadStateListener { state ->
                news.isVisible = state.refresh != LoadState.Loading
                swipeContainer.isRefreshing = state.refresh == LoadState.Loading
            }

            news.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { adapter.retry() },
                footer = NewsLoadStateAdapter { adapter.retry() }
            )

            swipeContainer.setOnRefreshListener { adapter.refresh() }
        }
        viewModel.setup()
    }

    private fun BaseMainActivityViewModel.setup() {

        news.observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                adapter.submitData(this@MainActivity.lifecycle, it)
            }.subscribe(this@MainActivity)
    }
}