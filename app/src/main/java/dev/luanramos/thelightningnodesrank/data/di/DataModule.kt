package dev.luanramos.thelightningnodesrank.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.luanramos.thelightningnodesrank.data.remote.datasource.LightningRemoteDataSource
import dev.luanramos.thelightningnodesrank.data.repository.LightningRepositoryImpl
import dev.luanramos.thelightningnodesrank.domain.repository.LightningRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mempool.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLightningRemoteDataSource(retrofit: Retrofit): LightningRemoteDataSource{
        return retrofit.create(LightningRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        lightningRemoteDataSource: LightningRemoteDataSource
    ): LightningRepository = LightningRepositoryImpl(lightningRemoteDataSource)

}