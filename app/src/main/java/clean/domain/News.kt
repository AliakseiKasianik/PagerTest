package clean.domain

data class News(
    val source: Source?,
    val title: String,
    val url: String?,
    val description: String?,
    val author: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
)

data class Source(
    val id: String?,
    val name: String,
)
