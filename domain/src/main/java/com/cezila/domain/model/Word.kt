package com.cezila.domain.model

import com.cezila.domain.error.WordError
import com.cezila.domain.Result

@JvmInline
value class Word private constructor(val value: String) {
    companion object {

        fun of(raw: String, requiredLength: Int = 5): Result<Word, WordError> {
            val normalized = normalize(raw)
            if (normalized.length != requiredLength ) {
                return Result.Error(WordError.INVALID_LENGTH)
            }
            if (!normalized.all { it in 'A'..'Z' }) {
                return Result.Error(WordError.INVALID_CHARS)
            }
            return Result.Success(Word(normalized))
        }

        fun normalize(input: String): String =
            input.uppercase()
                .replace(Regex("[ÁÂÃÀ]"), "A")
                .replace(Regex("[ÉÊÈ]"), "E")
                .replace(Regex("[ÍÎÌ]"), "I")
                .replace(Regex("[ÓÔÕÒ]"), "O")
                .replace(Regex("[ÚÛÙ]"), "U")
                .replace("Ç", "C")
                .replace(Regex("[^A-Z]"), "")
    }
}