import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import ui.MyWindow
import ui.components.MyApplicationState

fun main() = application {

    val applicationState = remember { MyApplicationState() }

    for (window in applicationState.windows) {
        key(window) {
            MyWindow(window)
        }
    }
}
