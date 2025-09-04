package com.cezila.domain.repository

import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.Result
import java.time.LocalDate

class FakeDailyWordRepository(
    private val mode: Mode
) : DailyWordRepository {

    private fun w(s: String): Word =
        when (val r = Word.of(s)) {
            is Result.Success -> r.data
            is Result.Error -> error("Word.of('$s') failed with ${r.error}")
        }

    enum class Mode { SUCCESS, COMMUNICATION_ERROR }

    override suspend fun fetchDaily(date: LocalDate): Result<Word, StartDailyRoundError> =
        when (mode) {
            Mode.SUCCESS -> Result.Success(w("SALAO"))
            Mode.COMMUNICATION_ERROR -> Result.Error(StartDailyRoundError.COMMUNICATION_ERROR)
        }

}