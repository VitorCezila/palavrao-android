package com.cezila.domain.use_case

import com.cezila.domain.repository.AvalancheGameProgressRepository

class UpdateGameResultUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    suspend operator fun invoke(gameWon: Boolean) {
        avalancheGameProgressRepository.updateAvalancheStats(gameWon)
        avalancheGameProgressRepository.clearAvalancheCurrentRound()
    }
}