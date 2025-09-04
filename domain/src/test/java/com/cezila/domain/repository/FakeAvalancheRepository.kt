package com.cezila.domain.repository

import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.Result

class FakeAvalancheRepository(
    private val mode: Mode
) : AvalancheRepository {

    enum class Mode { SUCCESS, COMMUNICATION_ERROR }

    private fun w(s: String): Word =
        when (val r = Word.of(s)) {
            is Result.Success -> r.data
            is Result.Error -> error("Word.of('$s') failed with ${r.error}")
        }

    override suspend fun fetchWord(): Result<Word, StartAvalancheRoundError> =
        when (mode) {
            Mode.SUCCESS -> Result.Success(w("SALTO"))
            Mode.COMMUNICATION_ERROR -> Result.Error(StartAvalancheRoundError.COMMUNICATION_ERROR)
        }
}