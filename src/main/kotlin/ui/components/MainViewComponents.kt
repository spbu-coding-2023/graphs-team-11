package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import data.Constants.APP_NAME
import data.Constants.CHOOSE_GRAPH_WINDOW_TITLE
import data.Constants.FILE_LOAD_FORMAT_FILTER
import data.graph_save.graphLoadUnified
import kotlinx.coroutines.CoroutineScope
import model.graph_model.Graph
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import viewmodel.MainVM
import java.awt.Dimension
import kotlin.reflect.KFunction3

class MyApplicationState(val scope: CoroutineScope) {
    val windows = mutableStateListOf<MyWindowState>()

    init {
        openChooseGraphWindow()
    }

    private fun openChooseGraphWindow() {
        windows += MyWindowState(CHOOSE_GRAPH_WINDOW_TITLE, scope = scope, isEmptyGraph = false)
    }

    private fun openNewWindow(graph: Graph?, scope: CoroutineScope, isEmptyGraph: Boolean) {
        windows += MyWindowState(APP_NAME, graph, scope, isEmptyGraph)
    }

    private fun exit() {
        windows.clear()
    }

    private fun MyWindowState(
        title: String, graph: Graph? = null, scope: CoroutineScope, isEmptyGraph: Boolean
    ) = MyWindowState(
        title,
        graph,
        openNewWindow = ::openNewWindow,
        exit = ::exit,
        windows,
        openChooseGraphWindow = ::openChooseGraphWindow,
        scope,
        isEmptyGraph,
    )
}

class MyWindowState(
    val title: String,
    val graph: Graph? = null,
    val openNewWindow: KFunction3<Graph?, CoroutineScope, Boolean, Unit>,
    val exit: () -> Unit,
    private val windows: SnapshotStateList<MyWindowState>,
    val openChooseGraphWindow: () -> Unit,
    val scope: CoroutineScope,
    isEmptyGraph: Boolean,
) {
    val mainVM = MainVM(graph, scope, isEmptyGraph)

    fun close() {
        val closeWindow = windows::remove
        if (windows.size == 1) {
            if (title != CHOOSE_GRAPH_WINDOW_TITLE) {
                closeWindow(this)
                openChooseGraphWindow()
            } else {
                closeWindow(this)
            }
        } else closeWindow(this)
    }

    fun reloadWindow(graph: Graph?, scope: CoroutineScope, isEmptyGraph: Boolean = false) {
        windows.remove(this)
        openNewWindow(graph, scope, isEmptyGraph)
    }
}

@Composable
fun SelectNameWindow(
    appTheme: MutableState<Theme>, viewModel: MainVM, onClose: () -> Unit
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
                    value = viewModel.graphName.value,
                    onValueChange = {
                        viewModel.onTextChange(it)
                    },
                    label = { Text("Name", color = MaterialTheme.colors.onPrimary) },
                    modifier = Modifier.padding(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                        unfocusedIndicatorColor = MaterialTheme.colors.onPrimary
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (!viewModel.isGraphNameAvailable.value) {
                    Text(
                        text = "Name already exists",
                        color = MaterialTheme.colors.error,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.wrapContentSize(unbounded = true)
                    )
                }
                Button(
                    onClick = {
                        viewModel.saveSQLiteGraph()
                        onClose()
                    }, enabled = viewModel.isGraphNameAvailable.value, colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                        disabledBackgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                    )
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun GraphFilePicker(
    isFileLoaderOpen: MutableState<Boolean>, fileLoaderException: MutableState<String?>, state: MyWindowState
) {
    FilePicker(
        isFileLoaderOpen.value, fileExtensions = FILE_LOAD_FORMAT_FILTER
    ) { path ->
        if (path != null) {
            try {
                val loadedGraph: Graph = graphLoadUnified(path.path)
                state.reloadWindow(loadedGraph, state.scope)
            } catch (e: NullPointerException) {
                fileLoaderException.value = e.message
            }
        }
        isFileLoaderOpen.value = false
    }
    if (fileLoaderException.value != null) {
        AlertDialog(
            title = { Text("Exception!") },
            text = { Text(fileLoaderException.value!!) },
            onDismissRequest = { fileLoaderException.value = null },
            confirmButton = {
                Button(onClick = {
                    fileLoaderException.value = null
                }) {
                    Text("Confirm")
                }
            },
        )
    }
}

@Composable
fun GraphLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary)
    }
}