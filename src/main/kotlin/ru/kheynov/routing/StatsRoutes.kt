package ru.kheynov.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.data.domain.entities.StatsElement
import ru.kheynov.data.stats.StatsDataSource
import ru.kheynov.utils.BadRequest
import ru.kheynov.utils.Language

fun Route.setupStatsRoutes(
    statsDataSource: StatsDataSource,
) {
    route("/api/stats") {
        getStats(statsDataSource)
        postStats(statsDataSource)
    }
}


private fun Route.getStats(
    statsDataSource: StatsDataSource,
) {
    get {
        val language = when (call.request.queryParameters["lang"]) {
            "ru" -> Language.Russian
            "en" -> Language.English
            else -> {
                call.respond(HttpStatusCode.BadRequest, "UNKNOWN_LANGUAGE")
                return@get
            }
        }
        try {
            val stats = statsDataSource.getStats(language)
            call.respond(HttpStatusCode.OK, stats)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
            return@get
        }
    }
}

private fun Route.postStats(
    statsDataSource: StatsDataSource,
) {
    post {
        val language = when (call.request.queryParameters["lang"]) {
            "ru" -> Language.Russian
            "en" -> Language.English
            else -> {
                call.respond(HttpStatusCode.BadRequest, "UNKNOWN_LANGUAGE")
                return@post
            }
        }
        val stats: StatsElement
        try {
            stats = call.receive()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            return@post
        }
        try {
            val res = statsDataSource.postStats(language, stats)
            if (res)
                call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotAcceptable)
        } catch (e: Exception) {
            if (e is BadRequest)
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            else
                call.respond(HttpStatusCode.InternalServerError, e.message.toString())

            return@post
        }
    }
}