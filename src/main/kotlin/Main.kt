import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.application
import data.Constants.CHOOSE_GRAPH_WINDOW_TITLE
import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.connectConfig
import data.db.sqlite_exposed.getTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ui.IntroWindowView
import ui.MainWindow
import ui.components.MyApplicationState

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        connectConfig()
        connect()
    }
    job.join() // wait until child coroutine completes

    application {
        val scope = rememberCoroutineScope { Dispatchers.Default }
        val applicationState = remember { MyApplicationState(scope) }

        val isSettingMenuOpen = mutableStateOf(false)
        val appTheme = mutableStateOf(getTheme())

        for (window in applicationState.windows) {
            key(window) {
                if (window.title == CHOOSE_GRAPH_WINDOW_TITLE) {
                    IntroWindowView(window, isSettingMenuOpen, appTheme, scope)
                } else {
                    MainWindow(window, isSettingMenuOpen, appTheme)
                }
            }
        }
    }
}
