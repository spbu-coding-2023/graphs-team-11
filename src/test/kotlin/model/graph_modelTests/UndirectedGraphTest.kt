/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

package model.graph_modelTests

import model.graph_model.UndirectedGraph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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