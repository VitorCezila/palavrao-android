package com.cezila.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cezila.data.repository.avalanche.DataStoreAvalancheGameProgressRepository
import com.cezila.data.repository.avalanche.LocalRandomAvalancheRepository
import com.cezila.domain.repository.SettingsRepository
import com.cezila.domain.repository.avalanche.AvalancheGameProgressRepository
import com.cezila.domain.repository.avalanche.AvalancheRepository
import com.cezila.domain.use_case.avalanche.GetAvalancheStatsUseCase
import com.cezila.domain.use_case.avalanche.LoadCurrentAvalancheRoundUseCase
import com.cezila.domain.use_case.avalanche.SaveAvalancheRoundUseCase
import com.cezila.domain.use_case.avalanche.StartAvalancheRoundUseCase
import com.cezila.domain.use_case.avalanche.UpdateAvalancheGameResultUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AvalancheModule {

    @Provides
    @Singleton
    fun provideAvalancheRepository(app: Application, settingsRepository: SettingsRepository): AvalancheRepository =
        LocalRandomAvalancheRepository(app.assets, settingsRepository = settingsRepository)

    @Provides
    @Singleton
    fun provideAvalancheGameProgressRepository(
        json: Json,
        dataStore: DataStore<Preferences>
    ): AvalancheGameProgressRepository =
        DataStoreAvalancheGameProgressRepository(dataStore = dataStore, json = json)

    @Provides
    @Singleton
    fun provideStartAvalancheRoundUseCase(
        repo: AvalancheRepository
    ) = StartAvalancheRoundUseCase(avalancheRepository = repo)

    @Provides
    @Singleton
    fun provideGetAvalancheStatsUseCase(
        repo: AvalancheGameProgressRepository
    ) = GetAvalancheStatsUseCase(avalancheGameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideLoadCurrentAvalancheRoundUseCase(
        repo: AvalancheGameProgressRepository
    ) = LoadCurrentAvalancheRoundUseCase(avalancheGameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideSaveAvalancheRoundUseCase(
        repo: AvalancheGameProgressRepository
    ) = SaveAvalancheRoundUseCase(avalancheGameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideUpdateAvalancheGameResultUseCase(
        repo: AvalancheGameProgressRepository
    ) = UpdateAvalancheGameResultUseCase(avalancheGameProgressRepository = repo)

}