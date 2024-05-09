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
import model.graph_model.GrahpViewClass
import ui.components.MyWindowState
import ui.components.cosmetic.CommeticsMenu
import ui.theme.BdsmAppTheme
import ui.theme.Theme
import viewmodel.MainWindowVM

@Composable
fun MyWindow(
    state: MyWindowState
) {
    val viewModel = MainWindowVM()
    val windowState = rememberWindowState(size = DpSize(1200.dp, 760.dp))

    Window(onCloseRequest = state::close, title = state.title, state = windowState) {
        MenuBar {
            Menu("Window") {
                Item("New window", onClick = state.openNewWindow)
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

        }
        App(viewModel.graphView, viewModel.changedAlgo, viewModel.appTheme)
        if (viewModel.isSettingMenuOpen.value) {
            SettingsView(
                onClose = { viewModel.isSettingMenuOpen.value = false },
                viewModel.appTheme,
            )
        }
    }
}

@Composable
fun <D> App(graphView: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>, appTheme: MutableState<Theme>) {
    BdsmAppTheme(appTheme = appTheme.value) {
        Row {
            Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
                LeftMenu(graphView, changedAlgo)
                CommeticsMenu(graphView, changedAlgo)
            }
            Card {
                GrahpView(graphView, showNodes = true, changedAlgo)
            }
        }
    }
}