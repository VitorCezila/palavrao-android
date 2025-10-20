package com.cezila.domain.use_case

import com.cezila.domain.repository.AvalancheGameProgressRepository

class LoadCurrentRoundUseCase(
    private val avalancheGameProgressRepository: AvalancheGameProgressRepository
) {
    operator fun invoke() = avalancheGameProgressRepository.getAvalancheCurrentRound()
}