package com.cezila.domain.repository

import com.cezila.domain.model.Round
import com.cezila.domain.model.avalanche.AvalancheStats
import kotlinx.coroutines.flow.Flow

interface AvalancheGameProgressRepository {

    fun getAvalancheCurrentRound(): Flow<Round?>
    suspend fun saveAvalancheCurrentRound(round: Round)
    suspend fun clearAvalancheCurrentRound()

    fun getAvalancheStats(): Flow<AvalancheStats>
    suspend fun updateAvalancheStats(gameWon: Boolean)

}