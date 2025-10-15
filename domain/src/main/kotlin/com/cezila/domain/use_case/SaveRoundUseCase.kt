package com.cezila.domain.use_case

import com.cezila.domain.model.Round
import com.cezila.domain.repository.GameProgressRepository

class SaveRoundUseCase(
    private val gameProgressRepository: GameProgressRepository
) {
    suspend operator fun invoke(round: Round) =
        gameProgressRepository.saveCurrentRound(round = round)
}