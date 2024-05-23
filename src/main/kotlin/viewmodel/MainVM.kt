package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import data.db.sqlite_exposed.getAllGraphs
import data.db.sqlite_exposed.saveGraph
import model.graph_model.GraphViewClass
import model.graph_model.Graph

class MainVM<D>(
    passedGraph: Graph<D>?,
) {
    val changedAlgo = mutableStateOf(false)

    val isSavedGraphsOpen = mutableStateOf(false)
    val isSelectNameWindowOpen = mutableStateOf(false)
    val isFileLoaderOpen = mutableStateOf(false)
    val fileLoaderException: MutableState<String?> = mutableStateOf(null)

    val graphName = mutableStateOf("")
    val selected: SnapshotStateMap<D, Int> = mutableStateMapOf()

    var graphList: List<Triple<Int, Graph<*>, String>> = emptyList()

    var graph: Graph<D> = passedGraph ?: Graph()

    val graphView = GraphViewClass(graph)

    fun onUndoPressed() {
        changedAlgo.value = true
        graphView.comeBack()
    }

    fun saveSQLiteGraph(graph: Graph<*>) {
        saveGraph(
            graph, graphName.value.ifEmpty { "Graph " + (getAllGraphs() + 1).size }
        )
    }

    fun onSQLEViewGraphsPressed() {
        graphList = getAllGraphs()
        isSavedGraphsOpen.value = true
    }
}