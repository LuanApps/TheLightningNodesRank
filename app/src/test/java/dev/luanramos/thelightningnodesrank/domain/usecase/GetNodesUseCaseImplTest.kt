package dev.luanramos.thelightningnodesrank.domain.usecase

import dev.luanramos.thelightningnodesrank.domain.model.TranslatedField
import dev.luanramos.thelightningnodesrank.domain.repository.LightningRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import dev.luanramos.thelightningnodesrank.domain.model.Node
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class GetNodesUseCaseImplTest {

    private val repository: LightningRepository = mockk()
    private lateinit var useCase: GetNodesUseCase

    @Before
    fun setup() {
        useCase = GetNodesUseCaseImpl(repository)
    }

    @Test
    fun `invoke should return list of nodes from repository`() = runBlocking{
        coEvery { repository.getNodes() } returns DEFAULT_TEST_NODE

        val result = useCase()

        assertEquals(DEFAULT_TEST_NODE, result)
        coVerify(exactly = 1) { repository.getNodes() }
    }

    companion object {
        private val DEFAULT_TEST_NODE  = listOf(
            Node(
                publicKey = "key1",
                alias = "Node1",
                channels = 10,
                capacity = 500000,
                firstSeen = 1600000000,
                updatedAt = 1600005000,
                city = null,
                country = TranslatedField(null, "USA", null, null, null, null, null, null)
            )
        )
    }
}