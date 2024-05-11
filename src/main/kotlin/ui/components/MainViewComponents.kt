package ui.components

import androidx.compose.runtime.mutableStateListOf
import model.graph_model.Graph

class MyApplicationState {
    val windows = mutableStateListOf<MyWindowState>()
    private val appName = "BDSM Graphs"

    init {
        windows += MyWindowState(appName)
    }

    private fun openNewWindow(graph: Graph<Int>?) {
        windows += MyWindowState("$appName ${windows.size}", graph)
    }

    private fun exit() {
        windows.clear()
    }

    private fun MyWindowState(
        title: String,
        graph: Graph<Int>? = null
    ) = MyWindowState(
        title, graph, openNewWindow = ::openNewWindow, exit = ::exit, windows::remove
    )
}

class MyWindowState(
    val title: String,
    val graph: Graph<Int>? = null,
    val openNewWindow: (Graph<Int>?) -> Unit,
    val exit: () -> Unit,
    private val close: (MyWindowState) -> Unit
) {
    fun close() = close(this)
}