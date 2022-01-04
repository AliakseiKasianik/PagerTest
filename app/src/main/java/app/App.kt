package app

import android.app.Application
import koin.app
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@App)
            modules(app)
        }
    }
}