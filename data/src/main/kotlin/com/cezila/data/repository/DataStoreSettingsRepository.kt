package com.cezila.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cezila.domain.model.Language
import com.cezila.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
    }

    override fun getLanguage(): Flow<Language> {
        return dataStore.data.map { prefs ->
            val code = prefs[Keys.LANGUAGE] ?: Language.PT.code
            Language.entries.find { it.code == code } ?: Language.PT
        }
    }

    override suspend fun setLanguage(language: Language) {
        dataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.code
        }
    }
}