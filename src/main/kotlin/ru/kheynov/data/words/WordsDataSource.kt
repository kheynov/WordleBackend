package ru.kheynov.data.words

import ru.kheynov.data.domain.entities.Word
import ru.kheynov.utils.Language

interface WordsDataSource {
    suspend fun getWord(language: Language): Word

    suspend fun updateWord(language: Language): Word

    suspend fun checkWord(word: String, language: Language): Boolean
}