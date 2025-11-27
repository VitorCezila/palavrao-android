package com.cezila.domain.repository

import com.cezila.domain.model.Word

class FakeDictionaryRepository(private val valid: Set<String>) : DictionaryRepository {
    override fun isWordValid(word: Word): Boolean = word.value in valid
}