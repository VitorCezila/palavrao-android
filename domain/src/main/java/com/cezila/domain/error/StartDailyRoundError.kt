package com.cezila.domain.error

import com.cezila.domain.result.RootError

enum class StartDailyRoundError : RootError {
    NETWORK_ERROR,
    INVALID_DATE
}