package com.cezila.domain.use_case.avalanche

import com.cezila.domain.repository.avalanche.AvalancheGameProgressRepository

class GetAvalancheStatsUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    operator fun invoke() = avalancheGameProgressRepository.getAvalancheStats()
}