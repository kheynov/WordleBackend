package ru.kheynov

import io.ktor.server.application.*
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import ru.kheynov.data.stats.MongoStatsDataSource
import ru.kheynov.data.words.MongoWordsDataSource
import ru.kheynov.plugins.configureHTTP
import ru.kheynov.plugins.configureMonitoring
import ru.kheynov.routing.configureRouting
import ru.kheynov.plugins.configureSerialization

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val db = KMongo.createClient(
        connectionString = environment.config.property("mongo.connection_string").getString()
    ).coroutine.getDatabase("wordle")

    val statsDataSource = MongoStatsDataSource(db)
    val wordsDataSource = MongoWordsDataSource(db, onClear = {
        launch {
            statsDataSource.clearStats(it)
        }
    })

    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting(wordsDataSource, statsDataSource)
}
