package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

class BridgeFinding : Algoritm(null) {
    override fun <D> algoRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        val bridges = findBridges(graph)
        val updateNode: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        val updateVert: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()

        bridges.forEach { (u, v) ->
            updateVert.computeIfAbsent(u) { mutableMapOf() }[v] = VertViewUpdate(color = Color.Red, alpha = 1f)
            updateVert.computeIfAbsent(v) { mutableMapOf() }[u] = VertViewUpdate(color = Color.Red, alpha = 1f)
        }

        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }

    private fun <D> findBridges(graph: Graph<D>): List<Pair<D, D>> {
        val visited = mutableMapOf<D, Boolean>().withDefault { false }
        val discovery = mutableMapOf<D, Int>().withDefault { -1 }
        val low = mutableMapOf<D, Int>().withDefault { -1 }
        val parent = mutableMapOf<D, D?>().withDefault { null }
        val bridges = mutableListOf<Pair<D, D>>()
        var time = 0

        for (node in graph.vertices.keys) {
            if (!visited.getValue(node)) {
                time = bridgeDFS(graph, node, visited, discovery, low, parent, bridges, time)
            }
        }

        return bridges
    }

    private fun <D> bridgeDFS(
        graph: Graph<D>,
        u: D,
        visited: MutableMap<D, Boolean>,
        discovery: MutableMap<D, Int>,
        low: MutableMap<D, Int>,
        parent: MutableMap<D, D?>,
        bridges: MutableList<Pair<D, D>>,
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