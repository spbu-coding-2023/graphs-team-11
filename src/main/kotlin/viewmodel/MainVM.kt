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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import data.db.sqlite_exposed.deleteGraph
import data.db.sqlite_exposed.getAllGraphs
import data.db.sqlite_exposed.saveGraph
import kotlinx.coroutines.CoroutineScope
import model.graph_model.Graph
import model.graph_model.GraphViewClass
import ui.components.MyWindowState

class MainVM(
    passedGraph: Graph?, scope: CoroutineScope, isEmptyGraph: Boolean
) {
    val changedAlgo = mutableStateOf(false)

    val isSavedGraphsOpen = mutableStateOf(false)
    val isSelectNameWindowOpen = mutableStateOf(false)
    val isFileLoaderOpen = mutableStateOf(false)
    val fileLoaderException: MutableState<String?> = mutableStateOf(null)
    val graphIsReady = mutableStateOf(false)

    val isGraphNameAvailable = mutableStateOf(true)

    val graphName = mutableStateOf("")
    val selected: SnapshotStateMap<String, Int> = mutableStateMapOf()

    var graphList: SnapshotStateList<Triple<Int, Graph, String>> = getAllGraphs().toMutableStateList()

    var graph: Graph = passedGraph ?: Graph()

    val graphView = GraphViewClass(graph, scope = scope, isEmpty = isEmptyGraph) {
        graphIsReady.value = true
        changedAlgo.value = true
    }

    fun onUndoPressed() {
        changedAlgo.value = true
        graphView.comeBack()
    }

    fun saveSQLiteGraph() {
        saveGraph(graph, graphName.value.ifEmpty { "Graph " + graphList.size + 1 })
        graphList.clear()
        graphList.addAll(getAllGraphs().toMutableStateList())
    }

    fun onSQLEViewGraphsPressed() {
        isSavedGraphsOpen.value = true
    }

    fun onSaveGraphPressed() {
        isSelectNameWindowOpen.value = true
    }

    fun onGraphLoad(state: MyWindowState, graph: Graph) {
        state.reloadWindow(graph, state.scope)
    }

    fun onGraphDelete(id: Int) {
        deleteGraph(id)
        println(getAllGraphs().size)
        graphList.removeIf { it.first == id }
    }

    fun onTextChange(text: String) {
        if (text.length <= 40) {
            graphName.value = text
        }
        isGraphNameAvailable.value = !graphList.map { it.third }.contains(text)
    }
}