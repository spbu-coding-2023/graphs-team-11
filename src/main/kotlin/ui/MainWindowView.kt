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
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import data.graph_save.GraphLoaderUnified
import model.graph_model.Graph
import ui.components.MyWindowState
import ui.components.SelectNameWindow
import ui.components.cosmetic.CommeticsMenu
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import viewmodel.MainVM
import java.awt.Dimension

@Composable
fun MyWindow(
    state: MyWindowState,
    isSettingMenuOpen: MutableState<Boolean>,
    appTheme: MutableState<Theme>
) {
    val viewModel = state.mainVM
    val windowState = rememberWindowState(size = DpSize(1200.dp, 760.dp))

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        window.minimumSize = Dimension(800, 600)
        MenuBar {
            Menu("Window") {
                Item("New window", onClick = state.openChooseGraphWindow)
                Item("Exit", onClick = state.exit)
            }
            Menu("Edit") {
                Item("Undo", shortcut = viewModel.undoShortcut, onClick = { viewModel.onUndoPressed() })
                Item("Redo", shortcut = viewModel.redoShortcut, onClick = {})
                Item("Copy", shortcut = viewModel.copyShortcut, onClick = {})
            }

            Menu(state.title) {
                Item("Settings", onClick = { isSettingMenuOpen.value = true })
                Item("Load from file", onClick = { viewModel.isFileLoaderOpen.value = true })
            }

            Menu("SQLite Exposed") {
                Item("Save Graph", onClick = { viewModel.isSelectNameWindowOpen.value = true })
                Item("View Graphs", onClick = { viewModel.onSQLEViewGraphsPressed() })
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
        FilePicker(viewModel.isFileLoaderOpen.value, fileExtensions = viewModel.fileFormatFilter) { path ->
            if (path != null) {
                val loadedGraph: Graph<String> = GraphLoaderUnified(path.path).graph
                state.close()
                state.openNewWindow(loadedGraph)
            }
        }
    }
}

@Composable
fun <D> App(
    viewModel: MainVM<D>,
    changedAlgo: MutableState<Boolean>,
    appTheme: MutableState<Theme>
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