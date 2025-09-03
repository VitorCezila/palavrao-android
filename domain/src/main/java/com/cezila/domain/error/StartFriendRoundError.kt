package com.cezila.domain.error

import com.cezila.domain.result.RootError

enum class StartFriendRoundError : RootError {
    NOT_FOUND,
    UNAVAILABLE
}