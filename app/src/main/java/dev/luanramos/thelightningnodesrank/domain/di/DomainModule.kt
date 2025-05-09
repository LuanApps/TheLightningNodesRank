package dev.luanramos.thelightningnodesrank.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.luanramos.thelightningnodesrank.domain.repository.LightningRepository
import dev.luanramos.thelightningnodesrank.domain.usecase.GetNodesUseCase
import dev.luanramos.thelightningnodesrank.domain.usecase.GetNodesUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideGetNodeUseCase(
        repository: LightningRepository
    ): GetNodesUseCase {
        return GetNodesUseCaseImpl(repository)
    }

}