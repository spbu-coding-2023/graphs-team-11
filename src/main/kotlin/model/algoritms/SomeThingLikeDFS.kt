package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate


class SomeThingLikeDFS : Algoritm(null) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        val vertUpdate: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()
        val visited: MutableSet<String> = mutableSetOf()
        for ((i, verts) in graph.vertices) {
            vertUpdate[i] = mutableMapOf()
            for ((j, _) in verts) {
                vertUpdate[i]!![j] = VertViewUpdate(alpha = 0.5f, color = Color.White)
            }
        }

        fun DFS(parent: String) {
            for ((node, _) in graph.vertices.getOrDefault(parent, mutableSetOf())) {
                if (node !in visited) {
                    vertUpdate[parent]!!.set(node, VertViewUpdate(color = Color.Red))
                    visited.add(node)
                    DFS(node)
                }
            }
        }

        for ((i, _) in graph.vertices) {
            if (i !in visited) DFS(i)
        }

        return Update(vertViewUpdate = vertUpdate)
    }
}