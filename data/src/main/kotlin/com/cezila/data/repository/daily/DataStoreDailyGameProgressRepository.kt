package com.cezila.data.repository.daily

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cezila.domain.model.Round
import com.cezila.domain.model.daily.DailyStats
import com.cezila.domain.repository.daily.DailyGameProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreDailyGameProgressRepository(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : DailyGameProgressRepository {

    private object Keys {
        val DAILY_CURRENT_ROUND = stringPreferencesKey("daily_current_round")
        val DAILY_STATS = stringPreferencesKey("daily_stats")
    }


    override fun getDailyCurrentRound(): Flow<Round?> {
        return dataStore.data.map { prefs ->
            prefs[Keys.DAILY_CURRENT_ROUND]?.let {
                json.decodeFromString<Round>(it)
            }
        }
    }

    override suspend fun saveDailyCurrentRound(round: Round) {
        dataStore.edit { prefs ->
            prefs[Keys.DAILY_CURRENT_ROUND] = json.encodeToString(round)
        }
    }

    override suspend fun clearDailyCurrentRound() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.DAILY_CURRENT_ROUND)
        }
    }

    override fun getDailyStats(): Flow<DailyStats> {
        return dataStore.data.map { prefs ->
            prefs[Keys.DAILY_STATS]?.let {
                json.decodeFromString<DailyStats>(it)
            } ?: DailyStats()
        }
    }

    override suspend fun updateDailyStats(gameWon: Boolean) {
        dataStore.edit { prefs ->
            val currentStatsJson = prefs[Keys.DAILY_STATS]
            val currentStats = currentStatsJson?.let {
                json.decodeFromString<DailyStats>(it)
            } ?: DailyStats()

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
            prefs[Keys.DAILY_STATS] = json.encodeToString(newStats)
        }
    }
}