package com.cezila.domain.use_case.avalanche

import com.cezila.domain.repository.avalanche.AvalancheGameProgressRepository

class LoadCurrentAvalancheRoundUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    operator fun invoke() = avalancheGameProgressRepository.getAvalancheCurrentRound()
}