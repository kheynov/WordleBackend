package ru.kheynov.data.words

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import ru.kheynov.utils.getExpirationTime
import ru.kheynov.utils.unixTime
import kotlin.random.Random

class MongoWordsDataSource(
    db: CoroutineDatabase,
) : WordsDataSource {
    private val ruWordsAll = db.getCollection<Word>("ru-all")
    private val enWordsAll = db.getCollection<Word>("en-all")

    private val ruWordsQuiz = db.getCollection<Word>("ru-quiz")
    private val enWordsQuiz = db.getCollection<Word>("en-quiz")

    private val ruHistory = db.getCollection<Word>("ru-history")
    private val enHistory = db.getCollection<Word>("en-history")


    override suspend fun getWord(language: Language): Word {
        val randomNumber =
            Random.nextInt((if (language == Language.Russian) ruWordsQuiz else enWordsQuiz).countDocuments().toInt())

        val lastWord = (if (language == Language.Russian) ruHistory else enHistory).find().first()
        if (lastWord != null) {
            val isNotExpired = lastWord.timeToNext!! > unixTime()
            if (isNotExpired) return lastWord
        }

        val word = when (language) {
            Language.Russian -> {
                val word =
                    ruWordsQuiz.find().limit(-1).skip(randomNumber).first() ?: throw Exception("Error: word not found")
                ruHistory.insertOne(word.apply {
                    timeToNext = getExpirationTime()
                    lang = language.code
                })
                word
            }
            Language.English -> {
                val word =
                    enWordsQuiz.find().limit(-1).skip(randomNumber).first() ?: throw Exception("Error: word not found")
                enHistory.insertOne(word.apply {
                    timeToNext = getExpirationTime()
                    lang = language.code
                })
                word
            }
        }
        return word
    }

    override suspend fun updateWord(language: Language): Word {
        (if (language == Language.Russian) ruHistory else enHistory).drop()
        return getWord(language)
    }

    override suspend fun checkWord(word: String, language: Language): Boolean {
        return (if (language == Language.Russian) ruWordsAll else enWordsAll).find(Word::word eq word).first() != null
    }

}