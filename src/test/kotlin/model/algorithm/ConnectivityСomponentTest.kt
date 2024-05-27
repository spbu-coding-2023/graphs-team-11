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

import model.algoritms.ConnectivityСomponent
import model.graph_model.Graph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConnectivityСomponentTest {

    private lateinit var conCom: ConnectivityСomponent
    private lateinit var graph: Graph

    @BeforeEach
    fun setup() {
        graph = Graph()
        conCom = ConnectivityСomponent()
    }

    @Test
    fun `getComponents of empty graph`() {
        val comp = conCom.getComponents(graph)

        assert(comp.isEmpty())
    }

    @Test
    fun `getComponents of graph without edges`() {
        val expectedComps = mutableSetOf<MutableSet<String>>()

        for (i in 1..10) {
            graph.addNode(i.toString())
            expectedComps.add(mutableSetOf(i.toString()))
        }

        assertEquals(conCom.getComponents(graph), expectedComps)
    }

    @Test
    fun `getComponents of graph without comps`() {
        val expectedComps = mutableSetOf<MutableSet<String>>(mutableSetOf("0"))

        graph.addNode("0")
        for (i in 1..10) {
            graph.addNode(i.toString())
            graph.addVertice((i - 1).toString(), i.toString())
            expectedComps.add(mutableSetOf(i.toString()))
        }
        graph.addVertice("0", "10")

        assertEquals(conCom.getComponents(graph), expectedComps)
    }

    @Test
    fun `getComponents of graph with one comp`() {
        val expectedComps = mutableSetOf<MutableSet<String>>(mutableSetOf("0"))

        graph.addNode("0")
        for (i in 1..10) {
            graph.addNode(i.toString())
            graph.addVertice((i - 1).toString(), i.toString())
            expectedComps.first().add(i.toString())
        }
        graph.addVertice("10", "0")

        assertEquals(conCom.getComponents(graph), expectedComps)
    }

    @Test
    fun `getComponents of graph with two comp`() {
        val expectedComps = mutableSetOf<MutableSet<String>>(
            mutableSetOf("0", "1", "2", "3", "4", "5"),
            mutableSetOf("6", "7", "8", "9", "10"),
        )

        graph.addNode("0")
        for (i in 1..10) {
            graph.addNode(i.toString())
            graph.addVertice((i - 1).toString(), i.toString())
        }
        graph.addVertice("5", "0")
        graph.addVertice("10", "6")

        assertEquals(conCom.getComponents(graph), expectedComps)
    }

    @Test
    fun `getComponents is equal for graph and reversed graph`() {
        assertEquals(conCom.getComponents(graph), conCom.getComponents(graph.reverse()))
    }
}