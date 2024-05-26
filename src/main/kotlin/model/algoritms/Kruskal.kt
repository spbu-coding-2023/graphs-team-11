package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

class Kruskal : Algoritm(null) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        val vertes: MutableList<Pair<Float, Pair<String, String>>> = mutableListOf()

        for ((node1, neighbourhood) in graph.vertices) {
            for ((node2, weight) in neighbourhood) {
                vertes.add(Pair(weight, Pair(node1, node2)))
            }
        }

        if (vertes.size <= 0) return Update()

        val treeVerts: MutableList<Pair<Float, Pair<String, String>>> = mutableListOf()
        val sortedVerts = vertes.sortedWith(compareBy { it.first })

        val visited: MutableSet<String> = mutableSetOf()

        treeVerts.add(sortedVerts[0])
        visited.add(sortedVerts[0].second.first)
        visited.add(sortedVerts[0].second.second)

        for (i in sortedVerts) {
            if ((i.second.first in visited).xor((i.second.second in visited))) {
                visited.add(i.second.first)
                visited.add(i.second.second)

                treeVerts.add(i)
            }
        }

        return getViewByTreeVert(treeVerts, graph)
    }

    fun getViewByTreeVert(treeVerts: MutableList<Pair<Float, Pair<String, String>>>, graph: Graph): Update {
        val updateNode: MutableMap<String, NodeViewUpdate> = mutableMapOf()
        val updateVert: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()

        for ((i, neighbourhood) in graph.vertices) {
            updateVert[i] = mutableMapOf()
            for ((j, weight) in neighbourhood) {
                updateVert[i]!![j] = VertViewUpdate(alpha = 0.1f)
            }
        }
        for ((weight, nodePair) in treeVerts) {
            if (nodePair.first !in updateVert) updateVert[nodePair.first] = mutableMapOf()
            updateVert[nodePair.first]!![nodePair.second] = VertViewUpdate(alpha = 1f)
        }
        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }
}