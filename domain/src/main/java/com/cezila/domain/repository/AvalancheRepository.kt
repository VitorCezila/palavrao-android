package com.cezila.domain.repository

import com.cezila.domain.model.Word
import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.result.Result

interface AvalancheRepository {
    suspend fun fetchWord(): Result<Word, StartAvalancheRoundError>
}