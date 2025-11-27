package com.cezila.domain.use_case.avalanche

import com.cezila.domain.model.Round
import com.cezila.domain.repository.avalanche.AvalancheGameProgressRepository

class SaveAvalancheRoundUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    suspend operator fun invoke(round: Round) =
        avalancheGameProgressRepository.saveAvalancheCurrentRound(round = round)
}