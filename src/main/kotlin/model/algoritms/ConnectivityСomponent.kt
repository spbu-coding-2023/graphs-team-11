package model.algoritms

import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import java.awt.Component
import kotlin.random.Random

class ConnectivityСomponent: Algoritm {
    override fun <D> alogRun(graph: Graph<D>): Update<D> {
        val reversed = graph.reverse()

        println(reversed.vertices)

        var visited: MutableSet<D> = mutableSetOf()
        val exitTime = mutableListOf<D>()

        fun DFSFirst(parent: D, grahp: Graph<D>) {
            visited.add(parent)
            // println(Pair(visited, exitTime))
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
        println(exitTime)
        fun DFSSecond(parent: D, grahp: Graph<D>, stack: MutableSet<D>) {
            // println(Pair(parent, stack))
            // println(visited)
            if (parent !in stack && parent !in visited) {
                visited.add(parent)
                stack.add(parent)
                for ((node, _) in grahp.vertices.getOrDefault(parent, mutableSetOf())) {
                    DFSSecond(node, grahp, stack)
                }
            }
        }

        val components: MutableSet<MutableSet<D>> = mutableSetOf()

        for (i in exitTime.reversed()) {
            if (i !in visited) {
                // println()
                val component = mutableSetOf<D>()
                DFSSecond(i, graph, component)
                // println(component)
                components.add(component)
            }
        }
        println(components)

        return getViewByComponents(components, graph)
    }

    fun <D> getViewByComponents(components: MutableSet<MutableSet<D>> = mutableSetOf(), graph: Graph<D>): Update<D> {
        val updateNode: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        val updateVert: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()

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