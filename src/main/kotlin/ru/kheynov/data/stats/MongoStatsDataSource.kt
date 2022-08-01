package ru.kheynov.data.stats

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import ru.kheynov.data.domain.entities.StatisticsResponse
import ru.kheynov.data.domain.entities.StatsElement
import ru.kheynov.utils.BadRequest
import ru.kheynov.utils.Language

class MongoStatsDataSource(
    db: CoroutineDatabase,
) : StatsDataSource {
    private val ruStats = db.getCollection<StatsElement>("ru-stats")
    private val enStats = db.getCollection<StatsElement>("en-stats")

    override suspend fun getStats(language: Language): StatisticsResponse {
        val attemptsStats = mutableListOf<Int>()
        val statsSource = if (language == Language.Russian) ruStats else enStats
        for (i in 0..6) {
            attemptsStats.add(statsSource.countDocuments(StatsElement::attempt eq i).toInt())
        }
        val appUsage = statsSource.countDocuments(StatsElement::app eq true).toInt()
        val webUsage = statsSource.countDocuments().toInt() - appUsage

        return StatisticsResponse(attempts = attemptsStats.toList(), app = appUsage, web = webUsage)
    }


    override suspend fun postStats(language: Language, statsRequest: StatsElement): Boolean {
        val statsSource = if (language == Language.Russian) ruStats else enStats
        if (statsRequest.attempt !in 0..6) {
            throw BadRequest("Wrong attempts number")
        }
        return statsSource.insertOne(statsRequest).wasAcknowledged()
    }

    override suspend fun clearStats(language: Language) {
        (if (language == Language.Russian) ruStats else enStats).drop()
    }
}

