package model.graph_modelTests

import model.graph_model.Graph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith


fun assertGraphInvariant(graph: Graph) {
    // Assert that all edges end in same graph's nodes
    for ((_, neighborhood) in graph.vertices) {
        for ((u, _) in neighborhood) {
            assert(u in graph.vertices)
        }
    }
}

class GraphTest {
    private lateinit var graph: Graph

    @BeforeEach
    fun setup() {
        graph = Graph()
    }

    @Test
    fun `getVertices on empty graph`() {
        assert(graph.vertices.isEmpty())
    }

    @Test
    fun `getVertices on not empty graph`() {

        // Ultimate graph with node with only out, in/out, only in, no in/out

        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        val expected = mutableMapOf(
            "1" to mutableSetOf(Pair("2", 1f)),
            "2" to mutableSetOf(Pair("3", 1f)),
            "3" to mutableSetOf(),
            "4" to mutableSetOf()
        )

        assertGraphInvariant(graph)
        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `addNode that not exist`() {
        graph.addNode("1")
        val expected = mutableMapOf("1" to mutableSetOf<Pair<String, Float>>())

        assertGraphInvariant(graph)
        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `addNode that exist`() {
        graph.addNode("1")
        graph.addNode("2")
        graph.addNode("2")
        val expected = mutableMapOf(
            "1" to mutableSetOf<Pair<String, Float>>(),
            "2" to mutableSetOf<Pair<String, Float>>()
        )
        assertGraphInvariant(graph)
        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `addVertice test when both nodes in graph`() {
        graph.addNode("1")
        graph.addNode("2")
        graph.addVertice("1", "2")

        val expected = mutableMapOf(
            "1" to mutableSetOf<Pair<String, Float>>(Pair("2", 1f)),
            "2" to mutableSetOf<Pair<String, Float>>()
        )
        assertGraphInvariant(graph)

        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `addVertice test when not both nodes in graph`() {
        graph.addNode("1")
        graph.addNode("2")
        graph.addVertice("3", "four")

        val expected = mutableMapOf(
            "1" to mutableSetOf<Pair<String, Float>>(),
            "2" to mutableSetOf<Pair<String, Float>>()
        )
        assertGraphInvariant(graph)

        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `deleteVertice that exist`() {
        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        graph.deleteNode("2")

        val expected = mutableMapOf<String, MutableSet<Pair<String, Float>>>(
            "1" to mutableSetOf(),
            "3" to mutableSetOf(),
            "4" to mutableSetOf()
        )

        assertGraphInvariant(graph)
        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `deleteEdge that exist`() {
        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        graph.deleteVertice("2", "3")

        val expected = mutableMapOf<String, MutableSet<Pair<String, Float>>>(
            "1" to mutableSetOf(Pair("2", 1f)),
            "2" to mutableSetOf(),
            "3" to mutableSetOf(),
            "4" to mutableSetOf()
        )

        assertGraphInvariant(graph)
        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `deleteVert that don't exist`() {
        assertFailsWith<NullPointerException> { graph.deleteVertice("0", "1") }
    }

    @Test
    fun reverse() {
        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        val reversed = graph.reverse()

        val expected = mutableMapOf(
            "1" to mutableSetOf(),
            "2" to mutableSetOf(Pair("1", 1f)),
            "3" to mutableSetOf(Pair("2", 1f)),
            "4" to mutableSetOf()
        )

        assertGraphInvariant(graph)
        assertEquals(reversed.vertices, expected)
    }
}