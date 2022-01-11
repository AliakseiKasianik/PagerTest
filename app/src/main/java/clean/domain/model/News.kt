package clean.domain.model

data class News(
    val id: String,
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
