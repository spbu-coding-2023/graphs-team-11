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
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate


class SampleAlgo : Algoritm(null) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        val updateNode: MutableMap<String, NodeViewUpdate> = mutableMapOf()
        val updateVert: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()
        for (i in graph.vertices) {
            if (i.value.size % 2 == 1) {
                updateNode[i.key] = NodeViewUpdate(color = Color.Red, radius = i.value.size.toFloat() * 5 + 5)
            } else {
                updateNode[i.key] = NodeViewUpdate(color = Color.Green, radius = i.value.size.toFloat() * 5 + 5)
            }
            updateVert[i.key] = mutableMapOf()
            for (j in i.value) {
                updateVert[i.key]!![j.first] = VertViewUpdate(color = Color.Yellow)
            }
        }
        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }
}