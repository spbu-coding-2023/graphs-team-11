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
import model.graph_model.graph_model_actions.Update


abstract class Algoritm(open val selectedSizeRequired: Int?) {
    open fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        return Update()
    }
}

enum class AlgoritmType {
    DIRECTED, UNDIRECTED, BOTH
}

data class AlgorithmData(
    val name: String,
    val algo: Algoritm,
    val type: AlgoritmType
)
