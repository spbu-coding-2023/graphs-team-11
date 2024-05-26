package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

class BridgeFinding : Algoritm(null) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        val bridges = findBridges(graph)
        val updateNode: MutableMap<String, NodeViewUpdate> = mutableMapOf()
        val updateVert: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()

        bridges.forEach { (u, v) ->
            updateVert.computeIfAbsent(u) { mutableMapOf() }[v] = VertViewUpdate(color = Color.Red, alpha = 1f)
            updateVert.computeIfAbsent(v) { mutableMapOf() }[u] = VertViewUpdate(color = Color.Red, alpha = 1f)
        }

        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }

    fun findBridges(graph: Graph): List<Pair<String, String>> {
        val visited = mutableMapOf<String, Boolean>().withDefault { false }
        val discovery = mutableMapOf<String, Int>().withDefault { -1 }
        val low = mutableMapOf<String, Int>().withDefault { -1 }
        val parent = mutableMapOf<String, String?>().withDefault { null }
        val bridges = mutableListOf<Pair<String, String>>()
        var time = 0

        for (node in graph.vertices.keys) {
            if (!visited.getValue(node)) {
                time = bridgeDFS(graph, node, visited, discovery, low, parent, bridges, time)
            }
        }

        return bridges
    }

    private fun bridgeDFS(
        graph: Graph,
        u: String,
        visited: MutableMap<String, Boolean>,
        discovery: MutableMap<String, Int>,
        low: MutableMap<String, Int>,
        parent: MutableMap<String, String?>,
        bridges: MutableList<Pair<String, String>>,
        time: Int
    ): Int {
        var currentTime = time
        visited[u] = true
        discovery[u] = currentTime
        low[u] = currentTime
        currentTime++

        for ((v, _) in graph.vertices[u] ?: emptyList()) {
            if (!visited.getValue(v)) {
                parent[v] = u
                currentTime = bridgeDFS(graph, v, visited, discovery, low, parent, bridges, currentTime)

                low[u] = minOf(low.getValue(u), low.getValue(v))
                if (low.getValue(v) > discovery.getValue(u)) {
                    bridges.add(Pair(u, v))
                }
            } else if (v != parent.getValue(u)) {
                low[u] = minOf(low.getValue(u), discovery.getValue(v))
            }
        }
        return currentTime
    }
}