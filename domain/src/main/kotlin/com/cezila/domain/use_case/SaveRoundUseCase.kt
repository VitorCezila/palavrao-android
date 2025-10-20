package com.cezila.domain.use_case

import com.cezila.domain.model.Round
import com.cezila.domain.repository.AvalancheGameProgressRepository

class SaveRoundUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    suspend operator fun invoke(round: Round) =
        avalancheGameProgressRepository.saveAvalancheCurrentRound(round = round)
}