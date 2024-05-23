package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import data.Constants.LOAD_FROM_FILE_SHORTCUT
import data.Constants.NEW_WINDOW_SHORTCUT
import data.Constants.REDO_SHORTCUT
import data.Constants.SAVE_EXPOSED_SHORTCUT
import data.Constants.SETTINGS_SHORTCUT
import data.Constants.UNDO_SHORTCUT
import data.Constants.VIEW_EXPOSED_SHORTCUT
import ui.components.GraphFilePicker
import ui.components.MyWindowState
import ui.components.SelectNameWindow
import ui.components.cosmetic.CommeticsMenu
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import viewmodel.MainVM
import java.awt.Dimension

@Composable
fun MyWindow(
    state: MyWindowState, isSettingMenuOpen: MutableState<Boolean>, appTheme: MutableState<Theme>
) {
    val viewModel = state.mainVM
    val windowState = rememberWindowState(size = DpSize(1200.dp, 760.dp))

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        window.minimumSize = Dimension(800, 600)
        MenuBar {
            Menu("Window") {
                Item("New window", shortcut = NEW_WINDOW_SHORTCUT, onClick = state.openChooseGraphWindow)
                Item("Exit", onClick = state.exit)
            }
            Menu("Edit") {
                Item("Undo", shortcut = UNDO_SHORTCUT, onClick = { viewModel.onUndoPressed() })
                Item("Redo", shortcut = REDO_SHORTCUT, onClick = {})
            }

            Menu(state.title) {
                Item("Settings", shortcut = SETTINGS_SHORTCUT, onClick = { isSettingMenuOpen.value = true })
                Item("Load from file",
                    shortcut = LOAD_FROM_FILE_SHORTCUT,
                    onClick = { viewModel.isFileLoaderOpen.value = true })
            }

            Menu("SQLite Exposed") {
                Item("Save Graph",
                    shortcut = SAVE_EXPOSED_SHORTCUT,
                    onClick = { viewModel.isSelectNameWindowOpen.value = true })
                Item("View Graphs", shortcut = VIEW_EXPOSED_SHORTCUT, onClick = { viewModel.onSQLEViewGraphsPressed() })
            }

        }
        App(viewModel, viewModel.changedAlgo, appTheme)
        if (isSettingMenuOpen.value) {
            SettingsView(
                onClose = { isSettingMenuOpen.value = false },
                appTheme,
            )
        }
        if (viewModel.isSavedGraphsOpen.value) {
            SavedGraphsView(
                onClose = { viewModel.isSavedGraphsOpen.value = false }, appTheme, viewModel.graphList, state
            )
        }
        if (viewModel.isSelectNameWindowOpen.value) {
            SelectNameWindow(onClose = {
                viewModel.isSelectNameWindowOpen.value = false
            }, appTheme, viewModel.graphName, onSave = {
                viewModel.saveSQLiteGraph(viewModel.graph)
            })
        }

        GraphFilePicker(viewModel.isFileLoaderOpen, viewModel.fileLoaderException, state)
    }
}

@Composable
fun <D> App(
    viewModel: MainVM<D>, changedAlgo: MutableState<Boolean>, appTheme: MutableState<Theme>
) {
    BdsmAppTheme(appTheme = appTheme.value) {
        Row {
            Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
                LeftMenu(viewModel.graphView, changedAlgo, viewModel.selected)
                CommeticsMenu(viewModel.graphView, changedAlgo, viewModel.selected)
            }
            Card {
                GrahpView(viewModel.graphView, changedAlgo, viewModel.selected)
            }
        }
    }
}