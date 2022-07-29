package ru.kheynov.data.words

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Word(
    val word: String,
    var lang: String? = null,
    @SerialName("next") var timeToNext: Long? = null,
)