package dev.luanramos.thelightningnodesrank.domain.repository

import dev.luanramos.thelightningnodesrank.domain.model.Node

interface LightningRepository {

    suspend fun getNodes(): List<Node>

}