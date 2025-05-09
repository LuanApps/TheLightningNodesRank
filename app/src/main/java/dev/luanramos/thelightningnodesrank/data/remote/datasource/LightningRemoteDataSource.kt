package dev.luanramos.thelightningnodesrank.data.remote.datasource

import dev.luanramos.thelightningnodesrank.domain.model.Node
import retrofit2.http.GET

interface LightningRemoteDataSource{
    @GET("api/v1/lightning/nodes/rankings/connectivity")
    suspend fun getNodeRankings(): List<Node>
}