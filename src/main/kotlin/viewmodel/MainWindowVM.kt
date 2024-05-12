package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.getAllGraphs
import data.db.sqlite_exposed.saveGraph
import data.tools.graphGenerators.randomTree
import model.graph_model.GrahpViewClass
import model.graph_model.Graph
import ui.theme.Theme

class MainWindowVM(
    passedGraph: Graph<*>?
) {
    private val isMac = System.getProperty("os.name").lowercase().contains("mac")
    val appTheme = mutableStateOf(Theme.LIGHT)
    val changedAlgo = mutableStateOf(false)
    val isSettingMenuOpen = mutableStateOf(false)
    val isSavedGraphsOpen = mutableStateOf(false)
    val isSelectNameWindowOpen = mutableStateOf(false)
    val graphName = mutableStateOf("")

    val copyShortcut = if (isMac) KeyShortcut(Key.C, meta = true) else KeyShortcut(Key.C, ctrl = true)
    val undoShortcut = if (isMac) KeyShortcut(Key.Z, meta = true) else KeyShortcut(Key.Z, ctrl = true)
    val redoShortcut = if (isMac) KeyShortcut(Key.Z, shift = true, meta = true) else KeyShortcut(Key.Y, ctrl = true)
    private var sqliteDBConnected = false


    var graphList: List<Triple<Int, Graph<*>, String>> = emptyList()

    var graph = passedGraph ?: randomTree(5)

    val graphView = GrahpViewClass(graph)

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
            graph, if (graphName.value.isEmpty()) "Graph " + (getAllGraphs() + 1).size else graphName
                .value
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