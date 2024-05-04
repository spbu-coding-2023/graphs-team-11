import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.algoritms_view.AlgorithmMenu
import ui.graph_view.GrahpView
import ui.graph_view.GrahpViewComponent
import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.randomTree
import data.tools.graphGenerators.starUndirected
import data.algoritms.*

@Composable
@Preview
fun App() {

    val g = randomTree(10)

    val gv = GrahpView(g)

    MaterialTheme {
        Row{
            AlgorithmMenu(gv)

            Card {
                GrahpViewComponent(gv, showNodes = true)
            }
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "BDSM Graphs") {
        App()
    }
}
