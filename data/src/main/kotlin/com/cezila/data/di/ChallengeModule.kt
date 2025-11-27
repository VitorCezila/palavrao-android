package com.cezila.data.di

import com.cezila.data.repository.challenge.FriendChallengeRemoteRepository
import com.cezila.data.service.IChallengeService
import com.cezila.domain.repository.DictionaryRepository
import com.cezila.domain.repository.challenge.FriendChallengeRepository
import com.cezila.domain.use_case.challenge.CreateFriendChallengeUseCase
import com.cezila.domain.use_case.challenge.StartFriendRoundUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChallengeModule {

    @Provides
    @Singleton
    fun provideChallengeService(client: OkHttpClient): IChallengeService {
        return Retrofit.Builder()
            .baseUrl("${IChallengeService.BASE_URL}/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IChallengeService::class.java)
    }

    @Provides
    @Singleton
    fun provideChallengeRepository(
        api: IChallengeService
    ): FriendChallengeRepository {
        return FriendChallengeRemoteRepository(api)
    }

    @Provides
    @Singleton
    fun provideStartChallengeRoundUseCase(
        repo: FriendChallengeRepository
    ) = StartFriendRoundUseCase(friendChallengeRepository = repo)

    @Provides
    @Singleton
    fun provideCreateFriendChallengeUseCase(
        dictionaryRepository: DictionaryRepository,
        repo: FriendChallengeRepository
    ) = CreateFriendChallengeUseCase(
        dictionaryRepository = dictionaryRepository,
        friendChallengeRepository = repo
    )

}