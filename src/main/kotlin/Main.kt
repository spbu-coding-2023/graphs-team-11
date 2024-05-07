import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import ui.AlgorithmMenu
import model.graph_model.GrahpViewClass
import model.graph_model.GrahpView
import viewmodel.MainVM

@Composable
@Preview
fun<D> App(graphView: GrahpViewClass<D>) {
    val changedAlgo = remember { mutableStateOf(false) }

    MaterialTheme {
        Row {
                AlgorithmMenu(graphView, changedAlgo)


            Card {
                GrahpView(graphView, showNodes = true, changedAlgo)
            }
        }
    }

}

fun main() = application {

    val applicationState = remember { MyApplicationState() }

    for (window in applicationState.windows) {
        key(window) {
            MyWindow(window)
        }
    }
}

@Composable
private fun MyWindow(
    state: MyWindowState
) {
    val viewModel = MainVM()
    val windowState = rememberWindowState(size = DpSize(1200.dp, 760.dp))

    val graphView = viewModel.graphView

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        MenuBar {
            Menu("Window") {
                Item("New window", onClick = state.openNewWindow)
                Item("Exit", onClick = state.exit)
            }

        }
        App(graphView)
    }
}


private class MyApplicationState {
    val windows = mutableStateListOf<MyWindowState>()
    val appName = "BDSM Graphs"

    init {
        windows += MyWindowState(appName)
    }

    fun openNewWindow() {
        windows += MyWindowState("$appName ${windows.size}")
    }

    fun exit() {
        windows.clear()
    }

    private fun MyWindowState(
        title: String
    ) = MyWindowState(
        title,
        openNewWindow = ::openNewWindow,
        exit = ::exit,
        windows::remove
    )
}

private class MyWindowState(
    val title: String,
    val openNewWindow: () -> Unit,
    val exit: () -> Unit,
    private val close: (MyWindowState) -> Unit
) {
    fun close() = close(this)
}
