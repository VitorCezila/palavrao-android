package com.cezila.data.di

import com.cezila.domain.repository.AvalancheRepository
import com.cezila.domain.repository.DictionaryRepository
import com.cezila.domain.repository.GameProgressRepository
import com.cezila.domain.use_case.GetAvalancheStatsUseCase
import com.cezila.domain.use_case.LoadCurrentRoundUseCase
import com.cezila.domain.use_case.SaveRoundUseCase
import com.cezila.domain.use_case.StartAvalancheRoundUseCase
import com.cezila.domain.use_case.SubmitGuessUseCase
import com.cezila.domain.use_case.UpdateGameResultUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameModule {

    @Provides
    @Singleton
    fun provideStartAvalancheRoundUseCase(
        repo: AvalancheRepository
    ) = StartAvalancheRoundUseCase(avalancheRepository = repo)

    @Provides
    @Singleton
    fun provideSubmitGuessUseCase(
        dictionary: DictionaryRepository
    ) = SubmitGuessUseCase(dictionaryRepository = dictionary)

    @Provides
    @Singleton
    fun provideGetAvalancheStatsUseCase(
        repo: GameProgressRepository
    ) = GetAvalancheStatsUseCase(gameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideLoadCurrentRoundUseCase(
        repo: GameProgressRepository
    ) = LoadCurrentRoundUseCase(gameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideSaveRoundUseCase(
        repo: GameProgressRepository
    ) = SaveRoundUseCase(gameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideUpdateGameResultUseCase(
        repo: GameProgressRepository
    ) = UpdateGameResultUseCase(gameProgressRepository = repo)
}