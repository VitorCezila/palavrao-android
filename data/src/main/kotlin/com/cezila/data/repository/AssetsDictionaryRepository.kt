package com.cezila.data.repository

import android.content.res.AssetManager
import com.cezila.domain.model.Language
import com.cezila.domain.model.Word
import com.cezila.domain.repository.DictionaryRepository

class AssetsDictionaryRepository(
    private val assets: AssetManager
) : DictionaryRepository {

    private val dictionaryCache = mutableMapOf<Language, Set<String>>()

    override fun isWordValid(word: Word, language: Language): Boolean {
        val dict = getOrLoadDictionary(language)
        return word.value in dict
    }

    private fun getOrLoadDictionary(language: Language): Set<String> {
        if (dictionaryCache.containsKey(language)) {
            return dictionaryCache[language]!!
        }

        return try {
            val words = assets.open(language.dictionaryPath).bufferedReader().useLines { lines ->
                lines
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { Word.normalize(it) }
                    .filter { it.length == 5 && it.all { ch -> ch in 'A'..'Z' } }
                    .toSet()
            }
            dictionaryCache[language] = words
            words
        } catch (_: Exception) {
            emptySet()
        }
    }
}