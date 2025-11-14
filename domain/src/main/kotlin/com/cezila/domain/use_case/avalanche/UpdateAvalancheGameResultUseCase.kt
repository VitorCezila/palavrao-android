package com.cezila.domain.use_case.avalanche

import com.cezila.domain.repository.avalanche.AvalancheGameProgressRepository

class UpdateAvalancheGameResultUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    suspend operator fun invoke(gameWon: Boolean) {
        avalancheGameProgressRepository.updateAvalancheStats(gameWon)
        avalancheGameProgressRepository.clearAvalancheCurrentRound()
    }
}