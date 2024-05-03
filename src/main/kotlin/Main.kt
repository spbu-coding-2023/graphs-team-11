import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.algoritms_view.AlgorithmMenu
import components.graph_view.GrahpView
import components.graph_view.GrahpViewComponent
import data.tools.graphGenerators.flowerSnark

@Composable
@Preview
fun App() {

    val g = flowerSnark(100)

    val gv = GrahpView(g)

    MaterialTheme {
        Row{
            AlgorithmMenu()

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
