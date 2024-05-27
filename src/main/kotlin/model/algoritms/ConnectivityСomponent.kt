package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import kotlin.random.Random

class ConnectivityСomponent : Algoritm(null) {

    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        return getViewByComponents(getComponents(graph), graph)
    }

    fun getComponents(graph: Graph): MutableSet<MutableSet<String>> {
        val reversed = graph.reverse()

        var visited: MutableSet<String> = mutableSetOf()
        val exitTime = mutableListOf<String>()

        fun DFSFirst(parent: String, grahp: Graph) {
            visited.add(parent)
            for ((node, _) in grahp.vertices.getOrDefault(parent, mutableSetOf())) {
                if (node !in visited) {
                    visited.add(node)
                    DFSFirst(node, grahp)
                }
            }
            exitTime.add(parent)

        }
        for ((i, _) in reversed.vertices) {
            if (i !in visited) {
                DFSFirst(i, reversed)
            }
        }

        visited = mutableSetOf()
        fun DFSSecond(parent: String, grahp: Graph, stack: MutableSet<String>) {
            if (parent !in stack && parent !in visited) {
                visited.add(parent)
                stack.add(parent)
                for ((node, _) in grahp.vertices.getOrDefault(parent, mutableSetOf())) {
                    DFSSecond(node, grahp, stack)
                }
            }
        }

        val components: MutableSet<MutableSet<String>> = mutableSetOf()

        for (i in exitTime.reversed()) {
            if (i !in visited) {
                val component = mutableSetOf<String>()
                DFSSecond(i, graph, component)
                components.add(component)
            }
        }
        return components
    }

    fun getViewByComponents(components: MutableSet<MutableSet<String>> = mutableSetOf(), graph: Graph): Update {
        val updateNode: MutableMap<String, NodeViewUpdate> = mutableMapOf()
        val updateVert: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()

        for (component in components) {
            val compColor = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
            for (i in component) {
                updateNode[i] = NodeViewUpdate(color = compColor)
                updateVert[i] = mutableMapOf()
                for ((j, _) in graph.vertices.getOrDefault(i, mutableSetOf())) {
                    if (j in component) {
                        updateVert[i]!![j] = VertViewUpdate(color = compColor, alpha = 1f)
                    } else {
                        updateVert[i]!![j] = VertViewUpdate(color = Color.Gray, alpha = 0.1f)
                    }
                }
            }
        }
        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }

}