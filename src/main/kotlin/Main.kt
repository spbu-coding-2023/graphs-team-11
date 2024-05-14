import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
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
            if (window.title == "Choose Graph") {
                IntroWindowView(window, isSettingMenuOpen, appTheme)
            } else {
                MyWindow(window, isSettingMenuOpen, appTheme)
            }
        }
    }
}
