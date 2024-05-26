package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

import java.util.PriorityQueue

data class NodeDistance(val node: String, val distance: Float) : Comparable<NodeDistance> {
    override fun compareTo(other: NodeDistance): Int {
        return this.distance.compareTo(other.distance)
    }
}

class ShortestPathDetection : Algoritm(2) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        val start = selected.keys.first()
        val end = selected.keys.last()
        val (pathNodes, pathEdges) = dijkstra(graph, start, end)
        val updateNode: MutableMap<String, NodeViewUpdate> = mutableMapOf()
        val updateVert: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()

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

    private fun dijkstra(graph: Graph, start: String, end: String): Pair<List<String>, List<Pair<String, String>>> {
        val distances = mutableMapOf<String, Float>().withDefault { Float.MAX_VALUE }
        val previousNodes = mutableMapOf<String, String?>()
        val priorityQueue = PriorityQueue<NodeDistance>()

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

    private fun reconstructPath(
        previousNodes: Map<String, String?>, start: String, end: String
    ): Pair<List<String>, List<Pair<String, String>>> {
        val pathNodes = mutableListOf<String>()
        val pathEdges = mutableListOf<Pair<String, String>>()
        var currentNode: String? = end

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

