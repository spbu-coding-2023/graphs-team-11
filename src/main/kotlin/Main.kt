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
import ui.components.MyApplicationState
import ui.components.MyWindowState
import viewmodel.MainVM

@Composable
@Preview
fun<D> App(graphView: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>) {

    MaterialTheme {
        Row {
            AlgorithmMenu(graphView, changedAlgo)

            Card {
                GrahpView(graphView, showNodes = true, changedAlgo)
            }
        }
    }
}

@Composable
private fun MyWindow(
    state: MyWindowState
) {
    val viewModel = MainVM()
    val windowState = rememberWindowState(size = DpSize(1200.dp, 760.dp))

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        MenuBar {
            Menu("Window") {
                Item("New window", onClick = state.openNewWindow)
                Item("Exit", onClick = state.exit)
            }
            Menu("Edit") {
                Item("Undo", shortcut = viewModel.undoShortcut, onClick = { viewModel.onUndoPressed() })
                Item("Redo", shortcut = viewModel.redoShortcut, onClick = {})
                Item("Copy", shortcut = viewModel.copyShortcut, onClick = {})
            }

        }
        App(viewModel.graphView, viewModel.changedAlgo)
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
