package com.cezila.domain.use_case

import com.cezila.domain.repository.AvalancheGameProgressRepository

class GetAvalancheStatsUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    operator fun invoke() = avalancheGameProgressRepository.getAvalancheStats()
}