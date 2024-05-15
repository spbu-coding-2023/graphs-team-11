package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import model.graph_model.Graph
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import java.awt.Dimension

class MyApplicationState {
    val windows = mutableStateListOf<MyWindowState>()
    private val appName = "BDSM Graphs"

    init {
        openChooseGraphWindow()
    }

    private fun openChooseGraphWindow() {
        windows += MyWindowState("Choose Graph")
    }

    private fun openNewWindow(graph: Graph<*>?) {
        windows += MyWindowState(appName, graph)
    }

    private fun exit() {
        windows.clear()
    }

    private fun MyWindowState(
        title: String, graph: Graph<*>? = null
    ) = MyWindowState(
        title,
        graph,
        openNewWindow = ::openNewWindow,
        exit = ::exit,
        windows.size,
        windows::remove,
        openChooseGraphWindow = ::openChooseGraphWindow
    )
}

class MyWindowState(
    val title: String,
    val graph: Graph<*>? = null,
    val openNewWindow: (Graph<*>?) -> Unit,
    val exit: () -> Unit,
    private val windowCount: Int,
    private val close: (MyWindowState) -> Unit,
    val openChooseGraphWindow: () -> Unit,
) {
    fun close() {
        if (windowCount == 0) {
            if (title != "Choose Graph") {
                close(this)
                openChooseGraphWindow()
            } else {
                close(this)
            }
        } else close(this)
    }
}

@Composable
fun SelectNameWindow(
    onClose: () -> Unit, appTheme: MutableState<Theme>, graphName: MutableState<String>, onSave: () -> Unit
) {
    Window(
        title = "Select Graph Name",
        onCloseRequest = onClose,
        alwaysOnTop = true,
    ) {
        window.minimumSize = Dimension(600, 450)
        BdsmAppTheme(appTheme = appTheme.value) {
            Column(
                modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Enter name for the graph",
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.wrapContentSize(unbounded = true)
                )
                TextField(
                    value = graphName.value,
                    onValueChange = {
                        if (it.length <= 40) {
                            graphName.value = it
                        }
                    },
                    label = { Text("Name", color = MaterialTheme.colors.onPrimary) },
                    modifier = Modifier.padding(16.dp),
                )
                Button(onClick = {
                    onSave()
                    onClose()
                }) {
                    Text("Save", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}