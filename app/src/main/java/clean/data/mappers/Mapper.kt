package clean.data.mappers

import clean.data.database.model.NewsDb
import clean.data.database.model.SourceDb
import clean.data.network.model.NewsDto
import clean.data.network.model.ResponseDto
import clean.data.network.model.SourceDto
import clean.domain.model.News
import clean.domain.model.Response
import clean.domain.model.Source
import java.util.*

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
        content = content,
        id = UUID.randomUUID().toString()
    )
}

private fun SourceDto.toSource(): Source {
    return Source(id = id, name = name)
}

fun News.mapToNewsDb(): NewsDb {
    return NewsDb(
        newsId = id,
        source = source?.mapToSourceDb(),
        title = title,
        url = url,
        description = description,
        urlToImage = urlToImage,
        author = author,
        publishedAt = publishedAt,
        content = content
    )
}

private fun Source.mapToSourceDb(): SourceDb {
    return SourceDb(
        id = id,
        name = name,
    )
}