import androidx.compose.animation.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.algoritms_view.AlgoritmListComponent
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
//            Card(modifier = Modifier.fillMaxHeight()) {
            DraggableMenuContent()
//            }

            Card {
                GrahpViewComponent(gv, showNodes = true)
            }
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }

}


@Composable
fun DraggableMenuContent() {
    val menuWidth = 200.dp
    var menuVisible by remember { mutableStateOf(true) }
    val density = LocalDensity.current

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight().background(Color.White)) {

        AnimatedVisibility(visible = menuVisible, enter = slideInHorizontally {
            with(density) { -40.dp.roundToPx() }
        } + expandHorizontally(
            expandFrom = Alignment.Start
        ) + fadeIn(
            initialAlpha = 0.3f
        ), exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()) {
            Surface(
                modifier = Modifier.width(menuWidth).fillMaxHeight(),
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
            ) {
                AlgoritmListComponent()
            }
        }
        Icon(imageVector = if (menuVisible) Icons.AutoMirrored.Filled.KeyboardArrowLeft
        else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Menu",
            modifier = Modifier.size(30.dp).clickable {
                menuVisible = !menuVisible
            })
    }

}