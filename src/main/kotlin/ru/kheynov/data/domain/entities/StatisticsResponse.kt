package ru.kheynov.data.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponse(
    val attempts: List<Int>,
    val app: Int,
    val web: Int,
)
