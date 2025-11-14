package com.cezila.domain.repository

import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.Result
import com.cezila.domain.model.daily.DailyWordInfo
import com.cezila.domain.repository.daily.DailyWordRepository
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

    override suspend fun fetchDaily(): Result<DailyWordInfo, StartDailyRoundError> =
        when (mode) {
            Mode.SUCCESS -> Result.Success(
                DailyWordInfo(
                    word = w("TESTE"),
                    referenceDate = LocalDate.now().toString()
                )
            )
            Mode.COMMUNICATION_ERROR -> Result.Error(StartDailyRoundError.COMMUNICATION_ERROR)
        }

}