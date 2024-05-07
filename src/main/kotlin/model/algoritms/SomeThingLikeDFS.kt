package model.algoritms

import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

fun <D> SomethingLikeDFS(graph: Graph<D>): Update<D> {
    val vertUpdate: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()
    val visited: MutableSet<D> = mutableSetOf()
    for ((i, verts) in graph.vertices) {
        vertUpdate[i] = mutableMapOf()
        for ((j, _) in verts) {
            vertUpdate[i]!![j] = VertViewUpdate(alpha = 0.5f, color = Color.White)
        }
    }

    fun DFS(parent: D) {
        for ((node, _) in graph.vertices.getOrDefault(parent, mutableSetOf())) {
            if (node !in visited) {
                vertUpdate[parent]!![node] = VertViewUpdate(color = Color.Red)
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
