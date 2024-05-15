package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

class SomeThingLikeDFS : Algoritm(null) {
    override fun <D> alogRun(graph: Graph<D>, selected: SnapshotStateMap<D, Int>): Update<D> {
        var vertUpdate: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()
        var visited: MutableSet<D> = mutableSetOf()
        for ((i, verts) in graph.vertices) {
            vertUpdate[i] = mutableMapOf()
            for ((j, _) in verts) {
                vertUpdate[i]!!.set(j, VertViewUpdate(alpha = 0.5f, color = Color.White))
            }
        }

        fun DFS(parent: D) {
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

        return Update<D>(vertViewUpdate = vertUpdate)
    }
}