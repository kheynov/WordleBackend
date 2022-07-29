package ru.kheynov

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.data.words.Language
import ru.kheynov.data.words.WordsDataSource

fun Route.setupWordsRoutes(
    wordsDataSource: WordsDataSource,
) {
    route("/api/word") {
        getWord(wordsDataSource)
        checkWord(wordsDataSource)
        if (environment?.config?.property("server.isDebug")?.getString() == "true")
            resetWord(wordsDataSource)
    }
}

private fun Route.getWord(
    wordsDataSource: WordsDataSource,
) {
    get("get") {

        val language = when (call.request.queryParameters["lang"]) {
            "ru" -> Language.Russian
            "en" -> Language.English
            else -> {
                call.respond(HttpStatusCode.BadRequest, "UNKNOWN_LANGUAGE")
                return@get
            }
        }

        try {
            val word = wordsDataSource.getWord(language)
            call.respond(
                HttpStatusCode.OK, word
            )
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }
    }
}

private fun Route.checkWord(
    wordsDataSource: WordsDataSource,
) {
    get("check") {
        val language = when (call.request.queryParameters["lang"]) {
            "ru" -> Language.Russian
            "en" -> Language.English
            else -> {
                call.respond(HttpStatusCode.BadRequest, "UNKNOWN_LANGUAGE")
                return@get
            }
        }
        try {
            val checkResult =
                wordsDataSource.checkWord(call.request.queryParameters["word"].toString(), language = language)
            call.respond(HttpStatusCode.OK, mapOf("correct" to checkResult))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }
    }
}

private fun Route.resetWord(
    wordsDataSource: WordsDataSource,
) {
    delete("/reset") {
        val language = when (call.request.queryParameters["lang"]) {
            "ru" -> Language.Russian
            "en" -> Language.English
            else -> {
                call.respond(HttpStatusCode.BadRequest, "UNKNOWN_LANGUAGE")
                return@delete
            }
        }

        try {
            val newWord = wordsDataSource.updateWord(language)
            call.respond(HttpStatusCode.OK, newWord)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }
    }
}