package ru.kheynov.data.words

@kotlinx.serialization.Serializable
data class Word(
    val word: String,
    var lang: String? = null,
    var timeToNext: Long? = null,
)