package clean.data.network.model

import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("source")
    val source: SourceDto?,

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?,
)

data class SourceDto(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String,
)