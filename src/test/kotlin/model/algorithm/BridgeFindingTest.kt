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
import model.algoritms.BridgeFinding
import model.graph_model.UndirectedGraph
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test class for checking the correctness of the bridge search algorithm.
 * Also for checking that this algoritm is correctly integrated in graph's update system
 */
class BridgeFindingTest {

    /**
     * testFindBridgesNoBridges - test that the bridge-finding algorithm will not find bridges in a graph
     * where there are no bridges
     */
    @Test
    fun testFindBridgesNoBridges() {
        val graph = UndirectedGraph().apply {
            addNode("1")
            addNode("2")
            addNode("3")
            addVertice("1", "2")
            addVertice("2", "3")
            addVertice("3", "1")
        }
        val expected = emptyList<Pair<String, String>>()
        val bridgeFinding = BridgeFinding()
        val result = bridgeFinding.findBridges(graph)
        assertEquals(expected, result)
    }

    /**
     * `test detects single bridge` - test that the bridge-finding algorithm will find bridge in a graph
     * where there is single bridges
     */
    @Test
    fun `test detects single bridge`() {
        val graph = UndirectedGraph().apply {
            addNode("1")
            addNode("2")
            addVertice("1", "2")
        }
        val bridgeFinding = BridgeFinding()
        val bridges = bridgeFinding.findBridges(graph)

        assertEquals(1, bridges.size, "Should detect exactly one bridge")
        assertEquals(Pair("1", "2"), bridges[0], "The bridge between nodes 1 and 2 should be detected")
    }

    /**
     * `test detects bridges in complex graph` - test that the bridge-finding algorithm will find all bridges in a graph
     */
    @Test
    fun `test detects bridges in complex graph`() {
        val graph = UndirectedGraph().apply {
            addNode("1")
            addNode("2")
            addNode("3")
            addNode("4")
            addVertice("1", "2")
            addVertice("2", "3")
            addVertice("3", "4")
            addVertice("1", "3")
        }
        val bridgeFinding = BridgeFinding()
        val bridges = bridgeFinding.findBridges(graph)

        assertTrue(bridges.contains(Pair("3", "4")), "Should detect that the connection between 3 and 4 is a bridge")
    }

    /**
     * `test detects bridges in a disconnected graph` - test that the bridge-finding algorithm will find no bridges
     * between disconnected nodes
     */
    @Test
    fun `test detects bridges in a disconnected graph`() {
        val graph = UndirectedGraph().apply {
            addNode("1")
            addNode("2")
            addNode("3")
            addVertice("1", "2")
            // Node 3 is disconnected
        }
        val bridgeFinding = BridgeFinding()
        val bridges = bridgeFinding.findBridges(graph)

        // Check that the correct bridge is identified
        assertEquals(1, bridges.size, "Should detect exactly one bridge in a partially connected graph")
        assertTrue(
            bridges.contains(Pair("1", "2")) || bridges.contains(Pair("2", "1")),
            "The bridge between nodes 1 and 2 should be detected"
        )

        // Ensure that no bridges are incorrectly identified with the disconnected node
        assertFalse(
            bridges.any { it.first == "3" || it.second == "3" }, "No bridges should involve the disconnected node 3"
        )
    }

    /**
     * testAlgoRunNoBridges - test that the bridge-finding algorithm will return empty update
     * on graph without brides
     */
    @Test
    fun testAlgoRunNoBridges() {
        val graph = UndirectedGraph().apply {
            addNode("1")
            addNode("2")
            addNode("3")
            addVertice("1", "2")
            addVertice("2", "3")
            addVertice("3", "1")
        }
        val selected = mutableStateMapOf<String, Int>()
        val expected = Update(nodeViewUpdate = mutableMapOf(), vertViewUpdate = mutableMapOf())
        val result = BridgeFinding().algoRun(graph, selected)
        for ((key, value) in expected.vertViewUpdate) {
            for ((key2, value2) in value) {
                assertEquals(value2.color, result.vertViewUpdate[key]?.get(key2)?.color)
                assertEquals(value2.alpha, result.vertViewUpdate[key]?.get(key2)?.alpha)
            }
        }
    }

    /**
     * testAlgoRunWithBridges - test that the bridge-finding algorithm will return correct update
     */
    @Test
    fun testAlgoRunWithBridges() {
        val graph = UndirectedGraph().apply {
            addNode("1")
            addNode("2")
            addNode("3")
            addNode("4")
            addNode("5")
            addVertice("1", "2")
            addVertice("2", "3")
            addVertice("3", "1")
            addVertice("2", "4")
            addVertice("4", "5")
        }
        val selected = mutableStateMapOf<String, Int>()
        val expected = Update(
            nodeViewUpdate = mutableMapOf(), vertViewUpdate = mutableMapOf(
                "2" to mutableMapOf("4" to VertViewUpdate(color = Color.Red, alpha = 1f)),
                "4" to mutableMapOf("2" to VertViewUpdate(color = Color.Red, alpha = 1f)),
                "4" to mutableMapOf("5" to VertViewUpdate(color = Color.Red, alpha = 1f)),
                "5" to mutableMapOf("4" to VertViewUpdate(color = Color.Red, alpha = 1f))
            )
        )
        val result = BridgeFinding().algoRun(graph, selected)
        for ((key, value) in expected.vertViewUpdate) {
            for ((key2, value2) in value) {
                assertEquals(value2.color, result.vertViewUpdate[key]?.get(key2)?.color)
                assertEquals(value2.alpha, result.vertViewUpdate[key]?.get(key2)?.alpha)
            }
        }
    }
}