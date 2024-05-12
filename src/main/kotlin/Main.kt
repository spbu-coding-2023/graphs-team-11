import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
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
