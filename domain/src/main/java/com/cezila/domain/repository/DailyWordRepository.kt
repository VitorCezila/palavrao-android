package com.cezila.domain.repository

import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.Result
import java.time.LocalDate

interface DailyWordRepository {
    suspend fun fetchDaily(date: LocalDate): Result<Word, StartDailyRoundError>
}