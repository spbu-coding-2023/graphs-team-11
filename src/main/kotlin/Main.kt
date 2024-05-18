import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import data.Constants.CHOOSE_GRAPH_WINDOW_TITLE
import ui.IntroWindowView
import ui.MyWindow
import ui.components.MyApplicationState
import ui.theme.Theme

fun main() = application {
    val applicationState = remember { MyApplicationState() }

    val isSettingMenuOpen = mutableStateOf(false)
    val appTheme = mutableStateOf(Theme.LIGHT)

    for (window in applicationState.windows) {
        key(window) {
            if (window.title == CHOOSE_GRAPH_WINDOW_TITLE) {
                IntroWindowView(window, isSettingMenuOpen, appTheme)
            } else {
                MyWindow(window, isSettingMenuOpen, appTheme)
            }
        }
    }
}
