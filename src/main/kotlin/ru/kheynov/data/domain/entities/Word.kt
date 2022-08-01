package ru.kheynov.data.domain.entities

@kotlinx.serialization.Serializable
data class Word(
    val word: String,
    var lang: String? = null,
    var next: Long? = null,
)