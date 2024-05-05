import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.algoritms_view.AlgorithmMenu
import ui.graph_view.GrahpView
import ui.graph_view.GrahpViewComponent
import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.randomTree

@Composable
@Preview
fun App() {

    val g = randomTree(10)

    val gv = GrahpView(g)
    val changedAlgo = remember { mutableStateOf(false) }

    MaterialTheme {
        Row{
            AlgorithmMenu(gv, changedAlgo)

            Card {
                GrahpViewComponent(gv, showNodes = true, changedAlgo)
            }
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "BDSM Graphs") {
        App()
    }
}
