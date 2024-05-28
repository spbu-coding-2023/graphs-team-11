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