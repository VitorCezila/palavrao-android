package com.cezila.data.di

import com.cezila.domain.repository.AvalancheRepository
import com.cezila.domain.repository.DictionaryRepository
import com.cezila.domain.use_case.StartAvalancheRoundUseCase
import com.cezila.domain.use_case.SubmitGuessUseCase
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
    ) = StartAvalancheRoundUseCase(repo)

    @Provides
    @Singleton
    fun provideSubmitGuessUseCase(
        dictionary: DictionaryRepository
    ) = SubmitGuessUseCase(dictionary)

}