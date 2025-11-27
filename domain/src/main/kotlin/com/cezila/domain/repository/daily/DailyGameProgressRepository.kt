package com.cezila.domain.repository.daily

import com.cezila.domain.model.Round
import com.cezila.domain.model.daily.DailyStats
import kotlinx.coroutines.flow.Flow

interface DailyGameProgressRepository {

    fun getDailyCurrentRound(): Flow<Round?>
    suspend fun saveDailyCurrentRound(round: Round)
    suspend fun clearDailyCurrentRound()

    fun getDailyStats(): Flow<DailyStats>
    suspend fun updateDailyStats(gameWon: Boolean)
    suspend fun clearDailyStats()
}