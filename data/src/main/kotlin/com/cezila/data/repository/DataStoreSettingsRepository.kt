package com.cezila.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cezila.domain.model.Language
import com.cezila.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
    }

    override fun getLanguage(): Flow<Language> {
        return dataStore.data.map { prefs ->
            val savedCode = prefs[Keys.LANGUAGE]

            if (savedCode != null) {
                Language.entries.find { it.code == savedCode } ?: Language.PT
            } else {
                val systemLanguage = Locale.getDefault().language
                Log.i("DataStoreSettings", "System language: $systemLanguage")
                if (systemLanguage.equals("pt", ignoreCase = true)) {
                    Language.PT
                } else {
                    Language.EN
                }
            }
        }
    }

    override suspend fun setLanguage(language: Language) {
        dataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.code
        }
    }
}