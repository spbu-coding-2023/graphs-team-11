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
                for (i in 1..10) {
                    addNode(i)
                }
                addVertice(1, 2)
                addVertice(1, 4)
                addVertice(2, 3)
                addVertice(2, 5)
                addVertice(2, 7)
                addVertice(3, 4)
                addVertice(3, 5)
                addVertice(5, 8)
                addVertice(5, 6)
                addVertice(6, 9)
                addVertice(7, 8)
                addVertice(7, 10)
                addVertice(8, 9)
                addVertice(9, 10)

            }
            stringGraph = Graph<String>().apply {
                for (i in 'A'..'J') {
                    addNode(i.toString())
                }
                addVertice("A", "B")
                addVertice("A", "D")
                addVertice("B", "C")
                addVertice("B", "E")
                addVertice("B", "G")
                addVertice("C", "D")
                addVertice("C", "E")
                addVertice("E", "H")
                addVertice("E", "F")
                addVertice("F", "I")
                addVertice("G", "H")
                addVertice("G", "J")
                addVertice("H", "I")
                addVertice("I", "J")
            }
            floatGraph = Graph<Float>().apply {
                for (i in 1..10) {
                    addNode(i.toFloat())
                }
                addVertice(1f, 2f)
                addVertice(1f, 4f)
                addVertice(2f, 3f)
                addVertice(2f, 5f)
                addVertice(2f, 7f)
                addVertice(3f, 4f)
                addVertice(3f, 5f)
                addVertice(5f, 8f)
                addVertice(5f, 6f)
                addVertice(6f, 9f)
                addVertice(7f, 8f)
                addVertice(7f, 10f)
                addVertice(8f, 9f)
                addVertice(9f, 10f)
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
                "1:(2, 1.0);(4, 1.0)|2:(3, 1.0);(5, 1.0);(7, 1.0)|3:(4, 1.0);(5, 1.0)|4:|5:(8, 1.0);(6, 1.0)|6:(9, 1.0)|7:(8, 1.0);(10, 1.0)|8:(9, 1.0)|9:(10, 1.0)|10::10"
            assertEquals(expectedSerializedGraph, serializedGraph)
        }

        @Test
        fun `test serializeGraph function with String`() {
            val serializedGraph = serializeGraph(stringGraph)
            val expectedSerializedGraph =
                "A:(B, 1.0);(D, 1.0)|B:(C, 1.0);(E, 1.0);(G, 1.0)|C:(D, 1.0);(E, 1.0)|D:|E:(H, 1.0);(F, 1.0)|F:(I, 1.0)|G:(H, 1.0);(J, 1.0)|H:(I, 1.0)|I:(J, 1.0)|J::10"
            assertEquals(expectedSerializedGraph, serializedGraph)
        }

        @Test
        fun `test serializeGraph function with Float`() {
            val serializedGraph = serializeGraph(floatGraph)
            val expectedSerializedGraph =
                "1.0:(2.0, 1.0);(4.0, 1.0)|2.0:(3.0, 1.0);(5.0, 1.0);(7.0, 1.0)|3.0:(4.0, 1.0);(5.0, 1.0)|4.0:|5.0:(8.0, 1.0);(6.0, 1.0)|6.0:(9.0, 1.0)|7.0:(8.0, 1.0);(10.0, 1.0)|8.0:(9.0, 1.0)|9.0:(10.0, 1.0)|10.0::10"
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
