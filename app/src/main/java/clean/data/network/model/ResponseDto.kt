package clean.data.network.model

import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("articles")
    val articles: List<NewsDto>,
)