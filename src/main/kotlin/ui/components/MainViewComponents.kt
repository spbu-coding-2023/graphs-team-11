package ui.components

import androidx.compose.runtime.mutableStateListOf

class MyApplicationState {
    val windows = mutableStateListOf<MyWindowState>()
    private val appName = "BDSM Graphs"

    init {
        windows += MyWindowState(appName)
    }

    private fun openNewWindow() {
        windows += MyWindowState("$appName ${windows.size}")
    }

    private fun exit() {
        windows.clear()
    }

    private fun MyWindowState(
        title: String
    ) = MyWindowState(
        title, openNewWindow = ::openNewWindow, exit = ::exit, windows::remove
    )
}

class MyWindowState(
    val title: String, val openNewWindow: () -> Unit, val exit: () -> Unit, private val close: (MyWindowState) -> Unit
) {
    fun close() = close(this)
}