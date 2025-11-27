package com.cezila.domain.error

import com.cezila.domain.RootError

enum class SubmitGuessError : RootError {
    ROUND_FINISHED,
    INVALID_LENGTH,
    INVALID_CHARS,
    INVALID_WORD
}