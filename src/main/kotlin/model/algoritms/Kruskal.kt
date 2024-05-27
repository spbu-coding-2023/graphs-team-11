/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

class Kruskal : Algoritm(null) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        return getViewByTreeVert(getMinimalTree(graph), graph)
    }

    fun getMinimalTree(graph: Graph): MutableList<Pair<Float, Pair<String, String>>> {
        val vertes: MutableList<Pair<Float, Pair<String, String>>> = mutableListOf()

        for ((node1, neighbourhood) in graph.vertices) {
            for ((node2, weight) in neighbourhood) {
                vertes.add(Pair(weight, Pair(node1, node2)))
            }
        }

        if (vertes.size <= 0) return vertes

        val treeVerts: MutableList<Pair<Float, Pair<String, String>>> = mutableListOf()
        val sortedVerts = vertes.sortedWith(compareBy { it.first })


        val trees: MutableMap<String, MutableSet<String>> = mutableMapOf()

        for ((v, _) in graph.vertices) {
            trees[v] = mutableSetOf(v)
        }

        for (i in sortedVerts) {
            if (i.second.second !in trees[i.second.first]!!) {
                treeVerts.add(i)
                trees[i.second.first]!!.add(i.second.second)
                trees[i.second.second] = trees[i.second.first]!!
            }
        }

        return treeVerts
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