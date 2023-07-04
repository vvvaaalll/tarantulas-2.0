package hr.vloboda.tarantulas

import android.app.Application
import hr.vloboda.tarantulas.di.repositoryModule
import hr.vloboda.tarantulas.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Tarantulas : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG)
                    Level.ERROR
                else
                    Level.NONE
            )
            androidContext(this@Tarantulas)
            modules(
                repositoryModule,
                viewModelModule
            )
        }

    }

    companion object {
        lateinit var application: Application
    }
}