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

package viewmodel

import androidx.compose.runtime.MutableState
import data.db.sqlite_exposed.deleteGraph
import model.graph_model.Graph
import ui.components.MyWindowState

class SavedGraphsVM {

    fun onGraphLoad(state: MyWindowState, graph: Graph) {
        state.reloadWindow(graph, state.scope)
    }

    fun onGraphDelete(id: Int, graphList: MutableState<List<Triple<Int, Graph, String>>>) {
        deleteGraph(id)
        graphList.value = graphList.value.filter { it.first != id }
    }
}