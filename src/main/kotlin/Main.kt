import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.tools.graphGenerators.flowerSnark
import ui.algoritms_view.AlgorithmMenu
import ui.graph_view.GrahpViewClass
import ui.graph_view.GrahpView
import data.tools.graphGenerators.randomTree
import data.tools.graphGenerators.starUndirected

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
    Window(onCloseRequest = ::exitApplication, title = "BDSM Graphs") {
        App()
    }
}
