package com.cezila.domain.repository.daily

import com.cezila.domain.Result
import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.daily.DailyWordInfo

interface DailyWordRepository {
    suspend fun fetchDaily(): Result<DailyWordInfo, StartDailyRoundError>
}