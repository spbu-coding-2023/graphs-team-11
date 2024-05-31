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

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import model.algoritms.ShortestPathDetection
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.VertViewUpdate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShortestPathFindingTest {

    @Test
    fun testSimplePath() {
        val graph = Graph().apply {
            addNode("A")
            addNode("B")
            addNode("C")
            addVertice("A", "B", 1f)
            addVertice("B", "C", 1f)
        }
        val selected = mutableStateMapOf("A" to 1, "C" to 1)
        val algo = ShortestPathDetection()
        val update = algo.algoRun(graph, selected)

        val expectedNodeUpdate = mapOf(
            "A" to NodeViewUpdate(color = Color.Green),
            "B" to NodeViewUpdate(color = Color.Green),
            "C" to NodeViewUpdate(color = Color.Green)
        )
        val expectedVertUpdate = mapOf(
            "A" to mapOf("B" to VertViewUpdate(color = Color.Green, alpha = 1f)),
            "B" to mapOf("C" to VertViewUpdate(color = Color.Green, alpha = 1f))
        )
        assertEquals(expectedNodeUpdate, update.nodeViewUpdate)

        for ((from, _) in expectedVertUpdate) {
            assertTrue(expectedVertUpdate[from] != null)
            assertEquals(expectedVertUpdate[from]?.keys, update.vertViewUpdate[from]?.keys)
        }
    }

    @Test
    fun testMultiplePaths() {
        val graph = Graph().apply {
            addNode("A")
            addNode("B")
            addNode("C")
            addNode("D")
            addVertice("A", "B", 1f)
            addVertice("B", "C", 1f)
            addVertice("A", "C", 2f)
            addVertice("C", "D", 1f)
        }
        val selected = mutableStateMapOf("A" to 1, "D" to 1)
        val algo = ShortestPathDetection()
        val update = algo.algoRun(graph, selected)

        val expectedNodeUpdate = mapOf(
            "A" to NodeViewUpdate(color = Color.Green),
            "C" to NodeViewUpdate(color = Color.Green),
            "D" to NodeViewUpdate(color = Color.Green)
        )
        val expectedVertUpdate = mapOf(
            "A" to mapOf("C" to VertViewUpdate(color = Color.Green, alpha = 1f)),
            "C" to mapOf("D" to VertViewUpdate(color = Color.Green, alpha = 1f))
        )
        for ((from, _) in expectedNodeUpdate) {
            assertEquals(expectedNodeUpdate[from], update.nodeViewUpdate[from])
        }
        for ((from, _) in expectedVertUpdate) {
            assertTrue(expectedVertUpdate[from] != null)
            assertEquals(expectedVertUpdate[from]?.keys, update.vertViewUpdate[from]?.keys)
        }
    }

    @Test
    fun testNoPath() {
        val graph = Graph().apply {
            addNode("A")
            addNode("B")
            addNode("C")
            addVertice("A", "B", 1f)
        }
        val selected = mutableStateMapOf("A" to 1, "C" to 1)
        val algo = ShortestPathDetection()
        val update = algo.algoRun(graph, selected)

        val expectedNodeUpdate = emptyMap<String, NodeViewUpdate>()
        val expectedVertUpdate = emptyMap<String, MutableMap<String, VertViewUpdate>>()
        assertEquals(expectedNodeUpdate, update.nodeViewUpdate)
        assertEquals(expectedVertUpdate, update.vertViewUpdate)
    }

    @Test
    fun testSingleNode() {
        val graph = Graph().apply {
            addNode("A")
        }
        val selected = mutableStateMapOf("A" to 1, "A" to 1)
        val algo = ShortestPathDetection()
        val update = algo.algoRun(graph, selected)

        val expectedNodeUpdate = mapOf("A" to NodeViewUpdate(color = Color.Green))
        val expectedVertUpdate = emptyMap<String, MutableMap<String, VertViewUpdate>>()
        assertEquals(expectedNodeUpdate, update.nodeViewUpdate)
        assertEquals(expectedVertUpdate, update.vertViewUpdate)
    }

    @Test
    fun testDisconnectedGraph() {
        val graph = Graph().apply {
            addNode("A")
            addNode("B")
            addNode("C")
            addNode("D")
            addVertice("A", "B", 1f)
            addVertice("C", "D", 1f)
        }
        val selected = mutableStateMapOf("A" to 1, "D" to 1)
        val algo = ShortestPathDetection()
        val update = algo.algoRun(graph, selected)

        val expectedNodeUpdate = emptyMap<String, NodeViewUpdate>()
        val expectedVertUpdate = emptyMap<String, MutableMap<String, VertViewUpdate>>()
        assertEquals(expectedNodeUpdate, update.nodeViewUpdate)
        assertEquals(expectedVertUpdate, update.vertViewUpdate)
    }

    @Test
    fun testComplexGraph() {
        val graph = Graph().apply {
            addNode("A")
            addNode("B")
            addNode("C")
            addNode("D")
            addNode("E")
            addVertice("A", "B", 1f)
            addVertice("B", "C", 2f)
            addVertice("C", "D", 1f)
            addVertice("A", "D", 4f)
            addVertice("B", "E", 3f)
            addVertice("E", "D", 1f)
        }
        val selected = mutableStateMapOf("A" to 1, "D" to 1)
        val algo = ShortestPathDetection()
        val update = algo.algoRun(graph, selected)

        val expectedNodeUpdate = mapOf(
            "A" to NodeViewUpdate(color = Color.Green), "D" to NodeViewUpdate(color = Color.Green)
        )
        val expectedVertUpdate = mapOf(
            "A" to mapOf("D" to VertViewUpdate(color = Color.Green, alpha = 1f)),
        )
        assertEquals(expectedNodeUpdate, update.nodeViewUpdate)
        for ((from, _) in expectedVertUpdate) {
            assertTrue(expectedVertUpdate[from] != null)
            assertEquals(expectedVertUpdate[from]?.keys, update.vertViewUpdate[from]?.keys)
        }
    }

}