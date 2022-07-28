package ru.kheynov.data.words

interface WordsDataSource {
    suspend fun getWord(language: Language): Word

    suspend fun updateWord(language: Language): Word

    suspend fun checkWord(word: String, language: Language): Boolean
}