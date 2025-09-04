package com.cezila.domain.error

import com.cezila.domain.RootError

enum class StartFriendRoundError : RootError {
    NOT_FOUND,
    COMMUNICATION_ERROR
}