package com.cezila.domain.use_case

import com.cezila.domain.repository.GameProgressRepository

class GetAvalancheStatsUseCase(
    private val gameProgressRepository: GameProgressRepository
) {
    operator fun invoke() = gameProgressRepository.getAvalancheStats()
}