package com.cezila.domain.repository

import com.cezila.domain.model.Language
import com.cezila.domain.model.Word

interface DictionaryRepository {
    fun isWordValid(word: Word, language: Language): Boolean
}