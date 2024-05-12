package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.graph_model.GrahpViewClass
import model.graph_model.graph_model_actions.Update
import viewmodel.cosmetics.CosmeticWidgetsMV

class CosmeticVM {
    val menuWidth = 200.dp

    val isMenuVisible = mutableStateOf(true)
    val cosmeticWidgetsViewModels: MutableList<CosmeticWidgetsMV> = mutableListOf()

    fun toggleMenu() {
        isMenuVisible.value = !isMenuVisible.value
    }

    fun <D> createUpdate(grahpView: GrahpViewClass<D>) {
        var update: Update<D> = Update()
        for (cosVM in cosmeticWidgetsViewModels) {
            update += cosVM.getUpdate(graph = grahpView.graph)
            println(update)
        }
        grahpView.applyUpdate(update)
    }

}