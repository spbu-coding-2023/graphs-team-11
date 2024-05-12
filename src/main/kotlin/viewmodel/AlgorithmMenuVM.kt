package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.algoritms.Algoritm
import model.graph_model.GrahpViewClass
import model.graph_model.graph_model_actions.Update

class AlgorithmMenuVM {
    val menuWidth = 200.dp

    val isMenuVisible = mutableStateOf(true)

    fun toggleMenu() {
        isMenuVisible.value = !isMenuVisible.value
    }

    fun <D> runAlgorithm(algorithm: Algoritm, grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>) {
        val update = algorithm.alogRun(grahpViewClass.graph)
        grahpViewClass.applyUpdate(update)
        changedAlgo.value = true
    }

}