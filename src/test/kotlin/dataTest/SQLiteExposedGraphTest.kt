package dataTest

import data.db.sqlite_exposed.*
import model.graph_model.Graph

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SQLiteExposedGraphTest {

    companion object {
        lateinit var intGraph: Graph<Int>
        lateinit var stringGraph: Graph<String>
        lateinit var floatGraph: Graph<Float>

        @JvmStatic
        @BeforeAll
        fun setup() {
            intGraph = Graph<Int>().apply {
                for (i in 1..4) {
                    addNode(i)
                }
                addVertice(1, 2)
                addVertice(2, 3)
                addVertice(3, 4)

            }
            stringGraph = Graph<String>().apply {
                for (i in 'A'..'D') {
                    addNode(i.toString())
                }
                addVertice("A", "B")
                addVertice("A", "D")
                addVertice("B", "C")

            }
            floatGraph = Graph<Float>().apply {
                for (i in 1..4) {
                    addNode(i.toFloat())
                }
                addVertice(1f, 2f)
                addVertice(2f, 3f)
                addVertice(3f, 4f)
            }

            connect()
        }
    }

    @Nested
    inner class SerializeAndDeserializeTests {

        @Test
        fun `test serializeGraph function with Int`() {
            val serializedGraph = serializeGraph(intGraph)
            val expectedSerializedGraph =
                "1:(2, 1.0)|2:(3, 1.0)|3:(4, 1.0)|4::4"
            assertEquals(expectedSerializedGraph, serializedGraph)
        }

        @Test
        fun `test serializeGraph function with String`() {
            val serializedGraph = serializeGraph(stringGraph)
            val expectedSerializedGraph =
                "A:(B, 1.0);(D, 1.0)|B:(C, 1.0)|C:|D::4"
            assertEquals(expectedSerializedGraph, serializedGraph)
        }

        @Test
        fun `test serializeGraph function with Float`() {
            val serializedGraph = serializeGraph(floatGraph)
            val expectedSerializedGraph =
                "1.0:(2.0, 1.0)|2.0:(3.0, 1.0)|3.0:(4.0, 1.0)|4.0::4"
            assertEquals(expectedSerializedGraph, serializedGraph)
        }
    }

    private val graphs = listOf(
        Pair(intGraph, "`Test Int Graph`"),
        Pair(stringGraph, "`Test String Graph`"),
        Pair(floatGraph, "`Test Float Graph`"),
    )

    @TestFactory
    fun `test Save and Get graphs`(): List<DynamicTest> {
        return graphs.map { (graph, name) ->
            DynamicTest.dynamicTest("Test save and get graph with name $name") {
                saveGraph(graph, name)
                val savedGraphs = getAllGraphs()
                val savedGraph = savedGraphs.find { it.third == name } ?: run {
                    throw AssertionError("Graph with name $name not found")
                }
                val savedGraphData = savedGraph.second
                assertEquals(name, savedGraph.third)
                assertEquals(graph.size, savedGraphData.size)

                for (node in graph.vertices) {
                    val contains = savedGraphData.vertices.containsKey(node.key)
                    assertTrue(contains)
                    for (neighbor in node.value) {
                        assertTrue(savedGraphData.vertices[node.key]?.contains(neighbor) == true)
                    }
                }
            }
        }
    }

    @TestFactory
    fun `test Delete graphs`(): List<DynamicTest> {
        return graphs.map { (_, name) ->
            DynamicTest.dynamicTest("Test delete graph with name $name") {
                val savedGraphs = getAllGraphs()
                val savedGraph = savedGraphs.find { it.third == name } ?: run {
                    throw AssertionError("Graph with name $name not found")
                }
                val savedGraphId = savedGraph.first
                deleteGraph(savedGraphId)
                val graphsAfterDelete = getAllGraphs()
                val deletedGraph = graphsAfterDelete.find { it.first == savedGraphId }
                assertEquals(null, deletedGraph)
            }
        }
    }

}
