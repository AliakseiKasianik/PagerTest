package clean.presentation.screen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import clean.presentation.adapter.HomeNewsAdapter
import com.itexus.pagertest.R
import com.itexus.pagertest.databinding.MainActivityLayoutBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import utils.subscribe

class MainActivity : AppCompatActivity(R.layout.main_activity_layout) {


    private val binding by viewBinding(MainActivityLayoutBinding::bind)
    private val viewModel by viewModel<BaseMainActivityViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeNewsAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.news.adapter = adapter
        binding.news.setHasFixedSize(true)


        viewModel.setup()
    }

    private fun BaseMainActivityViewModel.setup() {


        news
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                adapter.submitData(this@MainActivity.lifecycle, it)
                Log.e("AAA", it.toString())
            }
            .doOnError {
                Log.e("AAA", it.toString())
            }
            .subscribe(this@MainActivity)
    }
}