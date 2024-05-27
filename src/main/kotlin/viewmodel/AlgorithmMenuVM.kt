package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.unit.dp
import model.algoritms.Algoritm
import model.graph_model.GraphViewClass

class AlgorithmMenuVM {
    val menuWidth = 250.dp
    val isMenuVisible = mutableStateOf(true)

    val isException = mutableStateOf(false)
    val exceptionMessage = mutableStateOf("")

    fun toggleMenu() {
        isMenuVisible.value = !isMenuVisible.value
    }

    fun runAlgorithm(
        algorithm: Algoritm,
        graphViewClass: GraphViewClass,
        changedAlgo: MutableState<Boolean>,
        selected: SnapshotStateMap<String, Int>
    ) {
        val update = algorithm.algoRun(graphViewClass.graph, selected)
        graphViewClass.applyUpdate(update)
        changedAlgo.value = true
    }

}