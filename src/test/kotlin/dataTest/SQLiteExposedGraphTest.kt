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

package dataTest

import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.deleteGraph
import data.db.sqlite_exposed.getAllGraphs
import data.db.sqlite_exposed.saveGraph
import data.db.sqlite_exposed.serializeGraph
import model.graph_model.Graph
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


/**
 * A test class to check the correct operation of the module for saving graphs in database.
 */
class SQLiteExposedGraphTest {

    var graph = Graph().apply {
        for (i in 'A'..'D') {
            addNode(i.toString())
        }
        addVertice("A", "B")
        addVertice("A", "D")
        addVertice("B", "C")
    }

    @BeforeTest
    fun setup() {
        connect()
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
