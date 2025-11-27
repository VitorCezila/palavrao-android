package com.cezila.domain.model

enum class Language(val code: String, val dictionaryPath: String) {
    PT(code = "pt-BR", dictionaryPath = "dict/ptbr_5.txt"),
    EN(code = "en", dictionaryPath = "dict/en_5.txt")
}