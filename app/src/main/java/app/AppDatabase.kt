package app

import androidx.room.Database
import androidx.room.RoomDatabase
import clean.data.database.api.NewsDao
import clean.data.database.model.NewsDb

@Database(entities = [NewsDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao() : NewsDao
}