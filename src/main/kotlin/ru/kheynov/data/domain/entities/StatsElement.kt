package ru.kheynov.data.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class StatsElement(
    val attempt: Int,
    val app: Boolean,
)
