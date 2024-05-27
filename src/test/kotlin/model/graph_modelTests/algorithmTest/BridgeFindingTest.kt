package model.graph_modelTests.algorithmTest

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import model.algoritms.BridgeFinding
import model.graph_model.UndirectedGraph
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import kotlin.test.*

class BridgeFindingTest {

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