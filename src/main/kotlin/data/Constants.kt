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

package data

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut

object Constants {
    const val APP_NAME = "BDSM Graphs"
    const val SQLITE_DB = "sqlite_exposed_graph.db"
    const val SQLITE_APP_CONFIG = "sqlite_exposed_app_config.db"
    const val CHOOSE_GRAPH_WINDOW_TITLE = "Choose Graph"

    val FILE_LOAD_FORMAT_FILTER = listOf("graphml")

    private val isMac = System.getProperty("os.name").lowercase().contains("mac")
    val UNDO_SHORTCUT = if (isMac) KeyShortcut(Key.Z, meta = true) else KeyShortcut(Key.Z, ctrl = true)
    val REDO_SHORTCUT = if (isMac) KeyShortcut(Key.Z, shift = true, meta = true) else KeyShortcut(Key.Y, ctrl = true)
    val SETTINGS_SHORTCUT = if (isMac) KeyShortcut(Key.Comma, meta = true) else KeyShortcut(Key.Comma, ctrl = true)
    val SAVE_EXPOSED_SHORTCUT = if (isMac) KeyShortcut(Key.S, meta = true) else KeyShortcut(Key.S, ctrl = true)
    val VIEW_EXPOSED_SHORTCUT =
        if (isMac) KeyShortcut(Key.V, meta = true, shift = true) else KeyShortcut(Key.V, ctrl = true, shift = true)
    val LOAD_FROM_FILE_SHORTCUT = if (isMac) KeyShortcut(Key.O, meta = true) else KeyShortcut(Key.O, ctrl = true)
    val SAVE_TO_FILE_SHORTCUT = if (isMac) KeyShortcut(Key.E, meta = true) else KeyShortcut(Key.E, ctrl = true)
    val NEW_WINDOW_SHORTCUT = if (isMac) KeyShortcut(Key.N, meta = true) else KeyShortcut(Key.N, ctrl = true)
}