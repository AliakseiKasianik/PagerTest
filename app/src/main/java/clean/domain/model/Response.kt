package clean.domain.model

data class Response(
    val status: String,
    val totalResults: Int,
    val message: String? = null,
    val listNews: List<News>,
)