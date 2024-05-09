package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.randomTree
import data.tools.graphGenerators.starDirected
import model.graph_model.GrahpViewClass
import ui.theme.Theme

class MainVM {
    private val isMac = System.getProperty("os.name").lowercase().contains("mac")
    val appTheme = mutableStateOf(Theme.LIGHT)
    val changedAlgo = mutableStateOf(false)
    val isSettingMenuOpen = mutableStateOf(true)

    val copyShortcut = if (isMac) KeyShortcut(Key.C, meta = true) else KeyShortcut(Key.C, ctrl = true)
    val undoShortcut = if (isMac) KeyShortcut(Key.Z, meta = true) else KeyShortcut(Key.Z, ctrl = true)
    val redoShortcut = if (isMac) KeyShortcut(Key.Z, shift = true, meta = true) else KeyShortcut(Key.Y, ctrl = true)

    val graph = flowerSnark(10)
    val graphView = GrahpViewClass(graph)

    fun onUndoPressed() {
        changedAlgo.value = true
        graphView.comeBack()
    }

    fun onSettingsPressed() {
        isSettingMenuOpen.value = true
    }

}