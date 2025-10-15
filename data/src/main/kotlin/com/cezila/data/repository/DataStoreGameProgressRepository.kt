package com.cezila.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cezila.domain.model.Round
import com.cezila.domain.model.avalanche.AvalancheStats
import com.cezila.domain.repository.GameProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreGameProgressRepository(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : GameProgressRepository {

    private object Keys {
        val CURRENT_ROUND = stringPreferencesKey("current_round")
        val AVALANCHE_STATS = stringPreferencesKey("avalanche_stats")
    }

    override fun getCurrentRound(): Flow<Round?> {
        return dataStore.data.map { prefs ->
            prefs[Keys.CURRENT_ROUND]?.let {
                json.decodeFromString<Round>(it)
            }
        }
    }

    override suspend fun saveCurrentRound(round: Round) {
        dataStore.edit { prefs ->
            prefs[Keys.CURRENT_ROUND] = json.encodeToString(round)
        }
    }

    override suspend fun clearCurrentRound() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.CURRENT_ROUND)
        }
    }

    override fun getAvalancheStats(): Flow<AvalancheStats> {
        return dataStore.data.map { prefs ->
            prefs[Keys.AVALANCHE_STATS]?.let {
                json.decodeFromString<AvalancheStats>(it)
            } ?: AvalancheStats()
        }
    }

    override suspend fun updateAvalancheStats(gameWon: Boolean) {
        dataStore.edit { prefs ->
            val currentStatsJson = prefs[Keys.AVALANCHE_STATS]
            val currentStats = currentStatsJson?.let {
                json.decodeFromString<AvalancheStats>(it)
            } ?: AvalancheStats()

            val newStats = if (gameWon) {
                currentStats.copy(
                    gamesPlayed = currentStats.gamesPlayed + 1,
                    currentStreak = currentStats.currentStreak + 1,
                    maxStreak = maxOf(currentStats.maxStreak, currentStats.currentStreak + 1)
                )
            } else {
                currentStats.copy(
                    gamesPlayed = currentStats.gamesPlayed + 1,
                    currentStreak = 0
                )
            }
            prefs[Keys.AVALANCHE_STATS] = json.encodeToString(newStats)
        }
    }
}