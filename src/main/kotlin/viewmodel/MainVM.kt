package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import data.db.sqlite_exposed.getAllGraphs
import data.db.sqlite_exposed.saveGraph
import kotlinx.coroutines.CoroutineScope
import model.graph_model.GraphViewClass
import model.graph_model.Graph

class MainVM<D>(
    passedGraph: Graph<D>?, scope: CoroutineScope
) {
    val changedAlgo = mutableStateOf(false)

    val isSavedGraphsOpen = mutableStateOf(false)
    val isSelectNameWindowOpen = mutableStateOf(false)
    val isFileLoaderOpen = mutableStateOf(false)
    val fileLoaderException: MutableState<String?> = mutableStateOf(null)

    private val graphNamesList = mutableListOf<String>()
    val isGraphNameAvailable = mutableStateOf(true)

    val graphName = mutableStateOf("")
    val selected: SnapshotStateMap<D, Int> = mutableStateMapOf()

    var graphList: List<Triple<Int, Graph<*>, String>> = getAllGraphs()

    var graph: Graph<D> = passedGraph ?: Graph()

    val graphView = GraphViewClass(graph, scope = scope)

    fun onUndoPressed() {
        changedAlgo.value = true
        graphView.comeBack()
    }

    fun saveSQLiteGraph() {
        saveGraph(graph, graphName.value.ifEmpty { "Graph " + graphList.size + 1 })
        graphList = getAllGraphs()
    }

    fun onSQLEViewGraphsPressed() {
        isSavedGraphsOpen.value = true
    }

    fun onSaveGraphPressed() {
        isSelectNameWindowOpen.value = true
        graphNamesList.addAll(graphList.map { it.third })
    }

    fun onTextChange(text: String) {
        if (text.length <= 40) {
            graphName.value = text
        }
        isGraphNameAvailable.value = !graphNamesList.contains(text)
    }
}