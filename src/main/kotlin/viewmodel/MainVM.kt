package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.getAllGraphs
import data.db.sqlite_exposed.saveGraph
import model.graph_model.GraphViewClass
import model.graph_model.Graph
import ui.theme.Theme

class MainVM<D>(
    passedGraph: Graph<D>?,
    var isSettingMenuOpen: MutableState<Boolean>,
) {
    private val isMac = System.getProperty("os.name").lowercase().contains("mac")
    val changedAlgo = mutableStateOf(false)

    val isSavedGraphsOpen = mutableStateOf(false)
    val isSelectNameWindowOpen = mutableStateOf(false)
    val isFileLoaderOpen = mutableStateOf(false)

    val graphName = mutableStateOf("")
    val selected: SnapshotStateMap<D, Int> = mutableStateMapOf()

    val copyShortcut = if (isMac) KeyShortcut(Key.C, meta = true) else KeyShortcut(Key.C, ctrl = true)
    val undoShortcut = if (isMac) KeyShortcut(Key.Z, meta = true) else KeyShortcut(Key.Z, ctrl = true)
    val redoShortcut = if (isMac) KeyShortcut(Key.Z, shift = true, meta = true) else KeyShortcut(Key.Y, ctrl = true)
    private var sqliteDBConnected = false

    var graphList: List<Triple<Int, Graph<*>, String>> = emptyList()

    var graph: Graph<D> = passedGraph ?: Graph()

    val graphView = GraphViewClass(graph)

    fun onUndoPressed() {
        changedAlgo.value = true
        graphView.comeBack()
    }

    fun onSettingsPressed() {
        isSettingMenuOpen.value = true
    }

    fun saveSQLiteGraph(graph: Graph<*>) {
        if (!sqliteDBConnected) {
            initSQLiteExposed()
        }

        saveGraph(
            graph, graphName.value.ifEmpty { "Graph " + (getAllGraphs() + 1).size }
        )
    }

    fun onSQLEViewGraphsPressed() {
        if (!sqliteDBConnected) {
            initSQLiteExposed()
        }
        graphList = getAllGraphs()
        isSavedGraphsOpen.value = true
    }

    private fun initSQLiteExposed() {
        connect()
        sqliteDBConnected = true
    }

}