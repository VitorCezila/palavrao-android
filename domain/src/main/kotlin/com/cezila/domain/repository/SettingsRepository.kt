package com.cezila.domain.repository

import com.cezila.domain.model.Language
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getLanguage(): Flow<Language>
    suspend fun setLanguage(language: Language)
}