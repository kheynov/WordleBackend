package ru.kheynov.routing

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.data.stats.MongoStatsDataSource
import ru.kheynov.data.words.WordsDataSource

fun Application.configureRouting(
    wordsDataSource: WordsDataSource,
    statsDataSource: MongoStatsDataSource,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        setupWordsRoutes(wordsDataSource)
        setupStatsRoutes(statsDataSource)

        static("/static") {
            resources("static")
        }
    }
}
