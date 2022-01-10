package clean.data

import clean.data.network.model.NewsDto
import clean.data.network.model.ResponseDto
import clean.data.network.model.SourceDto
import clean.domain.News
import clean.domain.Response
import clean.domain.Source


fun ResponseDto.mapToDomain(): Response {

    return Response(
        status = status,
        totalResults = totalResults,
        message = message,
        listNews = articles.map {
            it.mapToNews()
        }
    )
}

fun NewsDto.mapToNews(): News {
    return News(
        source = this.source?.toSource(),
        title = title,
        url = url,
        description = description,
        author = author,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

private fun SourceDto.toSource(): Source {
    return Source(id = id, name = name)
}