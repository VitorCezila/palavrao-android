package com.cezila.data.di

import android.app.Application
import com.cezila.data.repository.AssetsDictionaryRepository
import com.cezila.data.repository.LocalRandomAvalancheRepository
import com.cezila.domain.repository.AvalancheRepository
import com.cezila.domain.repository.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
}