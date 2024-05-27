package model.graph_model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class UndirectedGraphTest {

    @Test
    fun `addVertice on Undirected`() {
        val graph = UndirectedGraph()

        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        val expected = mutableMapOf<String, MutableSet<Pair<String, Float>>>(
            "1" to mutableSetOf(Pair("2", 1f)),
            "2" to mutableSetOf(Pair("1", 1f), Pair("3", 1f)),
            "3" to mutableSetOf(Pair("2", 1f)),
            "4" to mutableSetOf()
        )

        assertGraphInvariant(graph)
        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `deleteVertice on undirectedGraph`() {
        val graph = UndirectedGraph()

        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        graph.deleteVertice("2", "3")

        val expected = mutableMapOf<String, MutableSet<Pair<String, Float>>>(
            "1" to mutableSetOf(Pair("2", 1f)),
            "2" to mutableSetOf(Pair("1", 1f)),
            "3" to mutableSetOf(),
            "4" to mutableSetOf()
        )

        assertGraphInvariant(graph)
        assertEquals(graph.vertices, expected)
    }

    @Test
    fun `Undirected is invarianted by reverse`() {
        val graph = UndirectedGraph()

        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        assertEquals(graph.vertices, graph.reverse().vertices)
    }
}