package algorithm.main

import androidx.compose.ui.graphics.Color
import data.Graph
import ui.graph_view.graph_view_actions.Update
import ui.graph_view.graph_view_actions.VertViewUpdate

class SomeThingLikeDFS: Algoritm() {
    override fun <D> alogRun(graph: Graph<D>): Update<D> {
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