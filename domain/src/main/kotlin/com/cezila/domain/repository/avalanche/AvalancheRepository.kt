package com.cezila.domain.repository.avalanche

import com.cezila.domain.model.Word
import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.Result

interface AvalancheRepository {
    suspend fun fetchWord(): Result<Word, StartAvalancheRoundError>
}