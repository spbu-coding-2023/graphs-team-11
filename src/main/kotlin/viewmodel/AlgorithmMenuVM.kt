package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.graph_model.GrahpViewClass
import model.graph_model.graph_model_actions.Update

class AlgorithmMenuVM {
    val menuWidth = 200.dp

    val isMenuVisible = mutableStateOf(true)

    fun toggleMenu() {
        isMenuVisible.value = !isMenuVisible.value
    }

    fun<D> runAlgorithm(algorithm: Update<D>, grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>) {
        grahpViewClass.applyUpdate(algorithm)
        changedAlgo.value = true
    }

}