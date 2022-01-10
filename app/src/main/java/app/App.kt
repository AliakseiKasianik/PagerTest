package app

import android.app.Application
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import koin.app
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogging()
        startKoin {
            androidContext(this@App)
            modules(app)
        }
    }


    private fun initLogging() {
        val tree = Timber.DebugTree()
        Timber.plant(tree)
        RxJavaPlugins.setErrorHandler(Timber::e)
    }
}