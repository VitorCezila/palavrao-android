package com.cezila.domain.use_case

import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.AvalancheRepository
import com.cezila.domain.Result

class StartAvalancheRoundUseCase(
    private val avalancheRepository: AvalancheRepository
) {
    suspend operator fun invoke(): Result<Round, StartAvalancheRoundError> {
        return when (val wordRes = avalancheRepository.fetchWord()) {
            is Result.Error   -> Result.Error(wordRes.error)
            is Result.Success -> Result.Success(Round(target = wordRes.data, status = RoundStatus.IN_PROGRESS))
        }
    }
}