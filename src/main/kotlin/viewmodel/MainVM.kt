package viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import data.tools.graphGenerators.flowerSnark
import model.graph_model.GrahpViewClass
import model.graph_model.Graph
import model.graph_model.NodeViewClass
import ui.theme.Theme

class MainVM<D> {
    private val isMac = System.getProperty("os.name").lowercase().contains("mac")
    val appTheme = mutableStateOf(Theme.LIGHT)
    val changedAlgo = mutableStateOf(false)
    val isSettingMenuOpen = mutableStateOf(true)
    val selected = mutableStateMapOf<D, Boolean>()

    val copyShortcut = if (isMac) KeyShortcut(Key.C, meta = true) else KeyShortcut(Key.C, ctrl = true)
    val undoShortcut = if (isMac) KeyShortcut(Key.Z, meta = true) else KeyShortcut(Key.Z, ctrl = true)
    val redoShortcut = if (isMac) KeyShortcut(Key.Z, shift = true, meta = true) else KeyShortcut(Key.Y, ctrl = true)

    val graph: Graph<Int> = flowerSnark(10)
    val graphView: GrahpViewClass<Int> = GrahpViewClass(graph)

    fun onUndoPressed() {
        changedAlgo.value = true
        graphView.comeBack()
    }

    fun onSettingsPressed() {
        isSettingMenuOpen.value = true
    }

}