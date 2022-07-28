package ru.kheynov.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.data.words.WordsDataSource
import ru.kheynov.setupWordsRoutes

fun Application.configureRouting(
    wordsDataSource: WordsDataSource,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        setupWordsRoutes(wordsDataSource)
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
