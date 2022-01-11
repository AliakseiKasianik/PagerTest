package clean.data.database.api

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import clean.data.database.model.NewsDb
import io.reactivex.rxjava3.core.Completable

@Dao
interface NewsDao {

    @Insert(onConflict = REPLACE)
    fun insertNews(news: List<NewsDb>): Completable

    @Query("DELETE FROM News")
    fun clearDb(): Completable

    @Query("SELECT * FROM News")
    fun getNews(): PagingSource<Int, NewsDb>
}