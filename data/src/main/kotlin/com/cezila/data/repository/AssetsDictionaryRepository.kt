package com.cezila.data.repository

import android.content.res.AssetManager
import com.cezila.domain.model.Word
import com.cezila.domain.repository.DictionaryRepository

class AssetsDictionaryRepository(
    private val assets: AssetManager,
    private val path: String = "dict/ptbr_5.txt"
) : DictionaryRepository {

    private val words: Set<String> by lazy {
        assets.open(path).bufferedReader().useLines { lines ->
            lines.asSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { Word.normalize(it) }
                .filter { it.length == 5 && it.all { ch -> ch in 'A'..'Z' } }
                .toSet()
        }
    }

    override fun isWordValid(word: Word): Boolean = word.value in words

}