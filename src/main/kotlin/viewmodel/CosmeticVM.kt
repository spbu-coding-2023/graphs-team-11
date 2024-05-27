package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.graph_model.GraphViewClass
import model.graph_model.graph_model_actions.Update
import viewmodel.cosmetics.CosmeticWidgetsMV

class CosmeticVM {
    val menuWidth = 250.dp


    val isMenuVisible = mutableStateOf(true)
    val cosmeticWidgetsViewModels: MutableList<CosmeticWidgetsMV> = mutableListOf()

    fun toggleMenu() {
        isMenuVisible.value = !isMenuVisible.value
    }

    fun createUpdate(grahpView: GraphViewClass) {
        var update = Update()
        for (cosVM in cosmeticWidgetsViewModels) {
            update += cosVM.getUpdate(graph = grahpView.graph)
        }
        grahpView.applyUpdate(update)
    }

}