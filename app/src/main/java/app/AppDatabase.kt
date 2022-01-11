package app

import androidx.room.Database
import androidx.room.RoomDatabase
import clean.data.database.api.NewsDao
import clean.data.database.api.RemoteKeysDao
import clean.data.database.model.NewsDb
import clean.data.database.model.RemoteKeys

@Database(entities = [NewsDb::class, RemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao() : NewsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}