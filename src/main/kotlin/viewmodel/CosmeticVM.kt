/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

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