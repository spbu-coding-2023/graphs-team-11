package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.AlgoritmFinder
import model.graph_model.GrahpViewClass
import model.graph_model.graph_model_actions.Update
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class AlgorithmMenuVM {
    val menuWidth = 200.dp
    val algo = AlgoritmFinder()

    val isMenuVisible = mutableStateOf(true)

    fun toggleMenu() {
        isMenuVisible.value = !isMenuVisible.value
    }

    fun<D> runAlgorithm(algorithm: KClass<*>, grahpViewClass: GrahpViewClass<D>, changedAlgo: MutableState<Boolean>) {
        val runAlgo = algorithm.members.single { it.name == "alogRun" }
        val algoExpample = algorithm.createInstance()

        val update = runAlgo.call(algoExpample, grahpViewClass.graph) as Update<D>

        grahpViewClass.applyUpdate(update)
        changedAlgo.value = true
    }

}