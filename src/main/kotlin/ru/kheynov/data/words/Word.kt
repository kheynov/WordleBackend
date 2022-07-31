package ru.kheynov.data.words

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Word(
    val word: String,
    var lang: String? = null,
    var next: Long? = null,
)