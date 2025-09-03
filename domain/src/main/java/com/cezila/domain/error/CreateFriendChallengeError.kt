package com.cezila.domain.error

import com.cezila.domain.result.RootError

enum class CreateFriendChallengeError : RootError {
    INVALID_LENGTH,
    INVALID_CHARS,
    INVALID_WORD,
    UNAVAILABLE
}