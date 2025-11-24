package com.cezila.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cezila.data.repository.GoogleTimeRepository
import com.cezila.data.repository.daily.DailyWordRepositoryRemoteImpl
import com.cezila.data.repository.daily.DataStoreDailyGameProgressRepository
import com.cezila.data.service.IDailyService
import com.cezila.domain.repository.TimeRepository
import com.cezila.domain.repository.daily.DailyGameProgressRepository
import com.cezila.domain.repository.daily.DailyWordRepository
import com.cezila.domain.use_case.daily.GetDailyStatsUseCase
import com.cezila.domain.use_case.daily.LoadCurrentDailyRoundUseCase
import com.cezila.domain.use_case.daily.SaveDailyRoundUseCase
import com.cezila.domain.use_case.daily.FetchDailyRoundUseCase
import com.cezila.domain.use_case.daily.GetDailyGameSessionUseCase
import com.cezila.domain.use_case.daily.UpdateDailyGameResultUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DailyModule {

    @Provides
    @Singleton
    fun provideDailyService(client: OkHttpClient): IDailyService {
        return Retrofit.Builder()
            .baseUrl("${IDailyService.BASE_URL}/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IDailyService::class.java)
    }

    @Provides
    @Singleton
    fun provideDailyRepository(
        api: IDailyService
    ): DailyWordRepository {
        return DailyWordRepositoryRemoteImpl(api)
    }

    @Provides
    @Singleton
    fun provideDailyGameProgressRepository(
        json: Json,
        dataStore: DataStore<Preferences>
    ): DailyGameProgressRepository =
        DataStoreDailyGameProgressRepository(dataStore = dataStore, json = json)

    @Provides
    @Singleton
    fun provideTimeRepository(@ApplicationContext context: Context): TimeRepository =
        GoogleTimeRepository(context)

    @Provides
    @Singleton
    fun provideStartDailyRoundUseCase(
        repo: DailyWordRepository
    ) = FetchDailyRoundUseCase(dailyWordRepository = repo)

    @Provides
    @Singleton
    fun provideGetDailyStatsUseCase(
        repo: DailyGameProgressRepository
    ) = GetDailyStatsUseCase(repository = repo)

    @Provides
    @Singleton
    fun provideLoadCurrentDailyRoundUseCase(
        gameProgressRepo: DailyGameProgressRepository
    ) = LoadCurrentDailyRoundUseCase(
        dailyGameProgressRepository = gameProgressRepo
    )

    @Provides
    @Singleton
    fun provideSaveAvalancheRoundUseCase(
        repo: DailyGameProgressRepository
    ) = SaveDailyRoundUseCase(dailyGameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideUpdateDailyGameResultUseCase(
        repo: DailyGameProgressRepository
    ) = UpdateDailyGameResultUseCase(dailyGameProgressRepository = repo)

    @Provides
    @Singleton
    fun provideDailyGameSessionUseCase(
        timeRepository: TimeRepository,
        localRepository: DailyGameProgressRepository,
        remoteRepository: DailyWordRepository
    ) = GetDailyGameSessionUseCase(
        timeRepository = timeRepository,
        localRepository = localRepository,
        remoteRepository = remoteRepository
    )

}