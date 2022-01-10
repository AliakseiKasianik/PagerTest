package clean.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "News")
data class NewsDb(

    @PrimaryKey(autoGenerate = true)
    val newsId: Long? = null,

    @Embedded
    val source: SourceDb?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "urlToImage")
    val urlToImage: String?,

    @ColumnInfo(name = "publishedAt")
    val publishedAt: String?,

    @ColumnInfo(name = "content")
    val content: String?,
)

data class SourceDb(
    val id: String?,
    val name: String,
)