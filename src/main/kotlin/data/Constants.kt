package data

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut

object Constants {
    const val APP_NAME = "BDSM Graphs"
    const val SQLITE_DB = "sqlite_exposed_graph.db"
    const val SQLITE_APP_CONFIG = "sqlite_exposed_app_config.db"
    const val CHOOSE_GRAPH_WINDOW_TITLE = "Choose Graph"

    val FILE_FORMAT_FILTER = listOf("graphml")

    private val isMac = System.getProperty("os.name").lowercase().contains("mac")
    val UNDO_SHORTCUT = if (isMac) KeyShortcut(Key.Z, meta = true) else KeyShortcut(Key.Z, ctrl = true)
    val REDO_SHORTCUT = if (isMac) KeyShortcut(Key.Z, shift = true, meta = true) else KeyShortcut(Key.Y, ctrl = true)
    val SETTINGS_SHORTCUT = if (isMac) KeyShortcut(Key.Comma, meta = true) else KeyShortcut(Key.Comma, ctrl = true)
    val SAVE_EXPOSED_SHORTCUT = if (isMac) KeyShortcut(Key.S, meta = true) else KeyShortcut(Key.S, ctrl = true)
    val VIEW_EXPOSED_SHORTCUT = if (isMac) KeyShortcut(Key.V, meta = true, shift = true) else KeyShortcut(Key.V, ctrl = true, shift = true)
    val LOAD_FROM_FILE_SHORTCUT = if (isMac) KeyShortcut(Key.O, meta = true) else KeyShortcut(Key.O, ctrl = true)
    val NEW_WINDOW_SHORTCUT = if (isMac) KeyShortcut(Key.N, meta = true) else KeyShortcut(Key.N, ctrl = true)
}