package koin

import androidx.room.Room
import app.AppDatabase
import clean.data.repository.RoomNewsRepository
import clean.domain.NewsDatabaseRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    val databaseName = "news_db"
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            databaseName
        ).build()
    }

    single<NewsDatabaseRepository> { RoomNewsRepository(dao = get()) }

    single { get<AppDatabase>().newsDao() }
}