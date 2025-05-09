package dev.luanramos.thelightningnodesrank.domain.usecase

import dev.luanramos.thelightningnodesrank.domain.model.Node

interface GetNodesUseCase {
    suspend operator fun invoke(): List<Node>
}