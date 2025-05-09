package dev.luanramos.thelightningnodesrank.data.repository

import dev.luanramos.thelightningnodesrank.data.remote.datasource.LightningRemoteDataSource
import dev.luanramos.thelightningnodesrank.domain.model.Node
import dev.luanramos.thelightningnodesrank.domain.repository.LightningRepository
import javax.inject.Inject

class LightningRepositoryImpl @Inject constructor(
    private val remoteDataSource: LightningRemoteDataSource
): LightningRepository {
    override suspend fun getNodes(): List<Node> {
        return remoteDataSource.getNodeRankings()
    }
}