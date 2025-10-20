package com.cezila.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.cezila.data.repository.AssetsDictionaryRepository
import com.cezila.data.repository.DataStoreAvalancheGameProgressRepository
import com.cezila.data.repository.LocalRandomAvalancheRepository
import com.cezila.domain.repository.AvalancheRepository
import com.cezila.domain.repository.DictionaryRepository
import com.cezila.domain.repository.AvalancheGameProgressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDictionaryRepository(app: Application): DictionaryRepository =
        AssetsDictionaryRepository(app.assets)

    @Provides
    @Singleton
    fun provideAvalancheRepository(app: Application): AvalancheRepository =
        LocalRandomAvalancheRepository(app.assets)

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    @Provides
    @Singleton
    fun provideGameProgressRepository(
        json: Json,
        dataStore: DataStore<Preferences>
    ): AvalancheGameProgressRepository =
        DataStoreAvalancheGameProgressRepository(dataStore = dataStore, json = json)
}