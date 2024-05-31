/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
