package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

import java.util.PriorityQueue

data class NodeDistance<D>(val node: D, val distance: Float) : Comparable<NodeDistance<D>> {
    override fun compareTo(other: NodeDistance<D>): Int {
        return this.distance.compareTo(other.distance)
    }
}

class ShortestPathDetection<D> : Algoritm<D>(2) {
    override fun <D> algoRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        val start = selected.keys.first()
        val end = selected.keys.last()
        val (pathNodes, pathEdges) = dijkstra(graph, start, end)
        val updateNode: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        val updateVert: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()

        for (node in pathNodes) {
            updateNode[node] = NodeViewUpdate(color = Color.Green)
        }

        for ((from, to) in pathEdges) {
            if (updateVert[from] == null) {
                updateVert[from] = mutableMapOf()
            }
            updateVert[from]?.set(to, VertViewUpdate(color = Color.Green, alpha = 1f))
        }

        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }

    private fun <D> dijkstra(graph: Graph<D>, start: D, end: D): Pair<List<D>, List<Pair<D, D>>> {
        val distances = mutableMapOf<D, Float>().withDefault { Float.MAX_VALUE }
        val previousNodes = mutableMapOf<D, D?>()
        val priorityQueue = PriorityQueue<NodeDistance<D>>()

        distances[start] = 0f
        priorityQueue.add(NodeDistance(start, 0f))

        while (priorityQueue.isNotEmpty()) {
            val (currentNode, currentDistance) = priorityQueue.poll()

            if (currentDistance > distances.getValue(currentNode)) continue

            for ((neighbor, weight) in graph.vertices[currentNode] ?: emptySet()) {
                val newDistance = currentDistance + weight

                if (newDistance < distances.getValue(neighbor)) {
                    distances[neighbor] = newDistance
                    previousNodes[neighbor] = currentNode
                    priorityQueue.add(NodeDistance(neighbor, newDistance))
                }
            }
        }

        val (pathNodes, pathEdges) = reconstructPath(previousNodes, start, end)
        return Pair(pathNodes, pathEdges)
    }

    private fun <D> reconstructPath(previousNodes: Map<D, D?>, start: D, end: D): Pair<List<D>, List<Pair<D, D>>> {
        val pathNodes = mutableListOf<D>()
        val pathEdges = mutableListOf<Pair<D, D>>()
        var currentNode: D? = end

        while (currentNode != null) {
            pathNodes.add(currentNode)
            val nextNode = previousNodes[currentNode]
            if (nextNode != null) {
                pathEdges.add(Pair(nextNode, currentNode))
            }
            currentNode = nextNode
        }

        pathNodes.reverse()
        pathEdges.reverse()

        return if (pathNodes.firstOrNull() == start) Pair(pathNodes, pathEdges) else Pair(emptyList(), emptyList())
    }
}

