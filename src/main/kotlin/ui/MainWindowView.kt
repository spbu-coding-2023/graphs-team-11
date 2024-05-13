package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import model.graph_model.GrahpViewClass
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
    state: MyWindowState
) {
    val viewModel = MainVM(state.graph)
    val windowState = rememberWindowState(size = DpSize(1200.dp, 760.dp))

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        window.minimumSize = Dimension(800, 600)
        MenuBar {
            Menu("Window") {
                Item("New window", onClick = { state.openNewWindow(null) })
                Item("Exit", onClick = state.exit)
            }
            Menu("Edit") {
                Item("Undo", shortcut = viewModel.undoShortcut, onClick = { viewModel.onUndoPressed() })
                Item("Redo", shortcut = viewModel.redoShortcut, onClick = {})
                Item("Copy", shortcut = viewModel.copyShortcut, onClick = {})
            }

            Menu(state.title) {
                Item("Settings", onClick = { viewModel.onSettingsPressed() })
            }

            Menu("SQLite Exposed") {
                Item("Save Graph", onClick = { viewModel.isSelectNameWindowOpen.value = true })
                Item("View Graphs", onClick = { viewModel.onSQLEViewGraphsPressed() })
            }

        }
        App(viewModel, viewModel.changedAlgo, viewModel.appTheme)
        if (viewModel.isSettingMenuOpen.value) {
            SettingsView(
                onClose = { viewModel.isSettingMenuOpen.value = false },
                viewModel.appTheme,
            )
        }
        if (viewModel.isSavedGraphsOpen.value) {
            SavedGraphsView(
                onClose = { viewModel.isSavedGraphsOpen.value = false }, viewModel.appTheme, viewModel.graphList, state
            )
        }
        if (viewModel.isSelectNameWindowOpen.value) {
            SelectNameWindow(onClose = {
                viewModel.isSelectNameWindowOpen.value = false
            }, viewModel.appTheme, viewModel.graphName, onSave = {
                viewModel.saveSQLiteGraph(viewModel.graph as Graph<*>)
            })
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