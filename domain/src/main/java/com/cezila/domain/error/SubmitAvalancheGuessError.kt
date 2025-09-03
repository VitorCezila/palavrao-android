package com.cezila.domain.error

import com.cezila.domain.result.RootError

enum class SubmitAvalancheGuessError : RootError {
    ROUND_FINISHED,
    INVALID_LENGTH,
    INVALID_CHARS,
    INVALID_WORD
}