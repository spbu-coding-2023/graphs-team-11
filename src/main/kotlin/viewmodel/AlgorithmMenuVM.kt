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