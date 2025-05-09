package dev.luanramos.thelightningnodesrank.domain.usecase

import dev.luanramos.thelightningnodesrank.domain.model.Node
import dev.luanramos.thelightningnodesrank.domain.repository.LightningRepository
import javax.inject.Inject

internal class GetNodesUseCaseImpl @Inject constructor(
    private val repository: LightningRepository
) : GetNodesUseCase {

    override suspend fun invoke(): List<Node> {
        return repository.getNodes()
    }
}