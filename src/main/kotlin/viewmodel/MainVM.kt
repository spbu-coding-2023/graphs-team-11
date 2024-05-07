package viewmodel

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import data.tools.graphGenerators.randomTree
import model.graph_model.GrahpViewClass

class MainVM {
    val graph = randomTree(100)
    val graphView = GrahpViewClass(graph)
}