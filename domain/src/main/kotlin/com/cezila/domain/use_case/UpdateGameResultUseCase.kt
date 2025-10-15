package com.cezila.domain.use_case

import com.cezila.domain.repository.GameProgressRepository

class UpdateGameResultUseCase(
    private val gameProgressRepository: GameProgressRepository
) {
    suspend operator fun invoke(gameWon: Boolean) {
        gameProgressRepository.updateAvalancheStats(gameWon)
        gameProgressRepository.clearCurrentRound()
    }
}