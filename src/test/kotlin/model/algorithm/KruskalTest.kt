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

package model.algorithm

import model.algoritms.Kruskal
import model.graph_model.UndirectedGraph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KruskalTest {

    private lateinit var kruskal: Kruskal
    private lateinit var graph: UndirectedGraph

    @BeforeEach
    fun setup() {
        graph = UndirectedGraph()
        kruskal = Kruskal()
    }

    fun assertTree(tree: MutableList<Pair<Float, Pair<String, String>>>) {
        val visited = mutableSetOf<String>()
        if (tree.size != 0) {
            visited.add(tree.first().second.first)
            visited.add(tree.first().second.second)

            for (i in 1..<tree.size) {
                assert((tree[i].second.first in visited).xor(tree[i].second.second in visited))
                visited.add(tree[i].second.first)
                visited.add(tree[i].second.second)
            }

        } else {
            assert(true)
        }
    }

    @Test
    fun `getMinimalTree on empty graph`() {
        val tree = kruskal.getMinimalTree(graph)

        assert(tree.isEmpty())

    }

    @Test
    fun `getMinimalTree on graph without edges`() {
        graph.addNode("1")
        graph.addNode("2")
        val tree = kruskal.getMinimalTree(graph)

        assert(tree.isEmpty())

    }

    @Test
    fun `getMinimalTree on graph without weights`() {

        graph.addNode("1")
        graph.addNode("2")
        graph.addNode("3")

        graph.addVertice("1", "2")
        graph.addVertice("2", "3")
        graph.addVertice("3", "1")

        val tree = kruskal.getMinimalTree(graph)

        assertTree(tree)
        assert(tree.size == graph.vertices.size - 1)
    }

    @Test
    fun `getMinimalTree on graph weights`() {

        graph.addNode("1")
        graph.addNode("2")
        graph.addNode("3")
        graph.addNode("4")

        graph.addVertice("1", "2", 1f)
        graph.addVertice("2", "3", 2f)
        graph.addVertice("3", "1", 4f)
        graph.addVertice("1", "4", 3f)
        graph.addVertice("2", "4", 5f)
        graph.addVertice("3", "4", 6f)

        val tree = kruskal.getMinimalTree(graph)

        assertTree(tree)
        assert(tree.size == graph.vertices.size - 1)

        var sum = 0f
        for ((i, _) in tree) sum += i
        assertEquals(sum, 6f)
    }
}