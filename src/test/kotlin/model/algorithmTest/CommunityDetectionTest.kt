package model.algorithmTest

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.algoritms.LeidenAlgorithm
import model.algoritms.LeidenToRun
import model.graph_model.Graph
import org.junit.jupiter.api.BeforeAll
import kotlin.test.*

class CommunityDetectionTest {

    companion object {
        private lateinit var mockGraph: Graph
        private lateinit var mockGraphWithTwoCommunities: Graph
        private lateinit var disconnectedGraph: Graph

        @JvmStatic
        @BeforeAll
        fun setup() {
            mockGraph = Graph().apply {
                for (i in 1..5) {
                    addNode(i.toString())
                }

                addVertice("1", "2")
                addVertice("1", "3")
                addVertice("2", "3")
                addVertice("3", "4")
                addVertice("4", "5")
            }

            mockGraphWithTwoCommunities = Graph().apply {
                for (i in 1..4) {
                    addNode(i.toString())
                }

                addVertice("1", "4")
                addVertice("1", "2")
                addVertice("2", "3")
            }

            disconnectedGraph = Graph().apply {
                for (i in 1..4) {
                    addNode(i.toString())
                }
            }

        }
    }

    @Test
    fun `test detectCommunities with empty graph`() {
        val graph = Graph()
        val algorithm = LeidenAlgorithm(graph)
        assertTrue(algorithm.detectCommunities().isEmpty())
    }

    @Test
    fun `test detectCommunities with non-empty graph`() {
        val graph = mockGraph // This should be a predefined graph structure
        val algorithm = LeidenAlgorithm(graph)
        val communities = algorithm.detectCommunities()

        assertFalse(communities.isEmpty())
    }

    @Test
    fun `test color assignment uniqueness`() {
        val graph = mockGraph
        val leidenToRun = LeidenToRun()
        val update = leidenToRun.algoRun(graph, SnapshotStateMap())

        // Check if colors are unique per community
        val colors = update.nodeViewUpdate.mapValues { it.value.color }
        assertEquals(1, colors.values.toSet().size)
    }

    @Test
    fun `test edge between different communities is gray with 0,1 alpha`() {
        val graph = mockGraphWithTwoCommunities
        val leidenToRun = LeidenToRun()
        val update = leidenToRun.algoRun(graph, SnapshotStateMap())

        val edges = update.vertViewUpdate
        val edgeBtwCommunities =
            edges.filter { edge -> edge.value.values.any { it.color == Color.Gray && it.alpha == 0.1f } }

        assertEquals(1, edgeBtwCommunities.size)
    }

    @Test
    fun `test view updates correctly reflect community changes`() {
        val graph = mockGraph
        val leidenToRun = LeidenToRun()
        val update = leidenToRun.algoRun(graph, SnapshotStateMap())

        assertFalse(update.nodeViewUpdate.isEmpty())
        assertFalse(update.vertViewUpdate.isEmpty())
    }

    @Test
    fun `test local moving algorithm improvements`() {
        val graph = mockGraph
        val algorithm = LeidenAlgorithm(graph)
        val initialCommunities = algorithm.detectCommunities()
        val improved = algorithm.localMovingAlgorithm()

        if (improved) {
            assertNotEquals(initialCommunities, algorithm.detectCommunities())
        }
    }

    @Test
    fun `test disconnected graph`() {
        val graph = disconnectedGraph
        val algorithm = LeidenAlgorithm(graph)
        val communities = algorithm.detectCommunities()

        val leidenToRun = LeidenToRun()
        val update = leidenToRun.algoRun(graph, SnapshotStateMap())
        val colors = update.nodeViewUpdate.mapValues { it.value.color }
        // Assert that the communities have been formed, not the exact community mapping
        assertFalse(communities.isEmpty())

        assertEquals(colors.values.toSet().size, update.nodeViewUpdate.size)
    }

    @Test
    fun `test findBestCommunity with neighbors in same community`() {
        val graph = Graph().apply {
            addNode("1")
            addNode("2")
            addVertice("1", "2")
        }
        val algorithm = LeidenAlgorithm(graph).apply {
            communities["1"] = 1
            communities["2"] = 1
        }

        val bestCommunity = algorithm.findBestCommunity("1")
        assertEquals(1, bestCommunity, "Should identify correct best community when neighbors share it")
    }

    @Test
    fun `test findBestCommunity with neighbors in different communities`() {
        val graph = Graph().apply {
            addNode("1")
            addNode("2")
            addVertice("1", "2")
        }
        val algorithm = LeidenAlgorithm(graph).apply {
            communities["1"] = 1
            communities["2"] = 2
        }

        val bestCommunity = algorithm.findBestCommunity("1")
        assertNotEquals(1, bestCommunity, "Should not identify same community as best if neighbors are different")
    }

}