package ru.kheynov.data.stats

import ru.kheynov.data.domain.entities.StatisticsResponse
import ru.kheynov.data.domain.entities.StatsElement
import ru.kheynov.utils.Language

interface StatsDataSource {
    suspend fun getStats(language: Language): StatisticsResponse
    suspend fun postStats(language: Language, statsRequest: StatsElement): Boolean
    suspend fun clearStats(language: Language)
}