package com.cezila.domain.repository

import com.cezila.domain.model.Round
import com.cezila.domain.model.avalanche.AvalancheStats
import kotlinx.coroutines.flow.Flow

interface GameProgressRepository {

    fun getCurrentRound(): Flow<Round?>
    suspend fun saveCurrentRound(round: Round)
    suspend fun clearCurrentRound()

    fun getAvalancheStats(): Flow<AvalancheStats>
    suspend fun updateAvalancheStats(gameWon: Boolean)

}