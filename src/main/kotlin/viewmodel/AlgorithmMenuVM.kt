package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.unit.dp
import model.algoritms.Algoritm
import model.graph_model.GraphViewClass

class AlgorithmMenuVM {
    val menuWidth = 200.dp

    val isMenuVisible = mutableStateOf(true)

    val isException = mutableStateOf(false)
    val exceptionMessage = mutableStateOf("")

    fun toggleMenu() {
        isMenuVisible.value = !isMenuVisible.value
    }

    fun <D> runAlgorithm(
        algorithm: Algoritm,
        graphViewClass: GraphViewClass<D>,
        changedAlgo: MutableState<Boolean>,
        selected: SnapshotStateMap<D, Int>
    ) {
        val update = algorithm.algoRun(graphViewClass.graph, selected)
        graphViewClass.applyUpdate(update)
        changedAlgo.value = true
    }

}