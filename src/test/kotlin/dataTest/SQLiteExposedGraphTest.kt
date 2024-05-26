package dataTest

import data.db.sqlite_exposed.*
import model.graph_model.Graph
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SQLiteExposedGraphTest {

    companion object {
        var graph = Graph().apply {
            for (i in 'A'..'D') {
                addNode(i.toString())
            }
            addVertice("A", "B")
            addVertice("A", "D")
            addVertice("B", "C")
        }

        @JvmStatic
        @BeforeAll
        fun setup() {
            connect()
        }
    }

    @Test
    fun `test serializeGraph function with String`() {
        val serializedGraph = serializeGraph(graph)
        val expectedSerializedGraph = "A:(B, 1.0);(D, 1.0)|B:(C, 1.0)|C:|D::4"
        assertEquals(expectedSerializedGraph, serializedGraph)
    }

    @Test
    fun `test Save, Get then Delete graph`() {
        val graphName = "My graph"
        saveGraph(graph, graphName)
        val savedGraphs = getAllGraphs()
        val savedGraph = savedGraphs.find { it.third == graphName } ?: run {
            throw AssertionError("Graph with name $graphName not found")
        }
        val savedGraphData = savedGraph.second
        assertEquals(graphName, savedGraph.third)
        assertEquals(graph.size, savedGraphData.size)

        for (node in graph.vertices) {
            val contains = savedGraphData.vertices.containsKey(node.key)
            assertTrue(contains)
            for (neighbor in node.value) {
                assertTrue(savedGraphData.vertices[node.key]?.contains(neighbor) == true)
            }
        }
        // deleting the graph.
        val savedGraphId = savedGraph.first
        deleteGraph(savedGraphId)
        val graphsAfterDelete = getAllGraphs()
        val deletedGraph = graphsAfterDelete.find { it.first == savedGraphId }
        assertEquals(null, deletedGraph)
    }
}
