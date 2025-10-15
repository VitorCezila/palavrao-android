package com.cezila.domain.use_case

import com.cezila.domain.repository.GameProgressRepository

class LoadCurrentRoundUseCase(
    private val gameProgressRepository: GameProgressRepository
) {
    operator fun invoke() = gameProgressRepository.getCurrentRound()
}