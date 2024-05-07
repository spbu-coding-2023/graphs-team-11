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
import data.tools.graphGenerators.randomTree
import viewmodel.MainVM


@Composable
@Preview
fun App() {

    val g = randomTree(100)

    val gv = GrahpViewClass(g)
    val changedAlgo = remember { mutableStateOf(false) }

    MaterialTheme {
        Row {
            AlgorithmMenu(gv, changedAlgo)

            Card {
                GrahpView(gv, showNodes = true, changedAlgo)
            }
        }
    }

}

fun main() = application {
    val viewModel = MainVM()
    val windowState = rememberWindowState(size = DpSize(1200.dp, 760.dp))

    Window(
        onCloseRequest = ::exitApplication,
        title = viewModel.appName,
        state = windowState,
    ) {
        MenuBar {
            // there will be menu bar on the top left side
        }
        App()
    }
}
