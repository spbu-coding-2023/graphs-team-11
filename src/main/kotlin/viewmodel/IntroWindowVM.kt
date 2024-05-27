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
import data.db.sqlite_exposed.deleteGraph
import data.db.sqlite_exposed.getAllGraphs
import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.randomTree
import data.tools.graphGenerators.starDirected
import data.tools.graphGenerators.starUndirected
import kotlinx.coroutines.CoroutineScope
import model.graph_model.Graph
import ui.components.MyWindowState

class IntroWindowVM(
    var isSettingMenuOpen: MutableState<Boolean>, private val scope: CoroutineScope
) {
    var graphList: MutableState<List<Triple<Int, Graph, String>>> = mutableStateOf(getAllGraphs())
    val isFileLoaderOpen = mutableStateOf(false)
    val fileLoaderException: MutableState<String?> = mutableStateOf(null)
    val chosenGraph = mutableStateOf("Saved")
    val graphSize = mutableStateOf("")
    val chosenGenerator = mutableStateOf("Random Tree")
    val weightMax = mutableStateOf("1")

    fun onSettingsPressed() {
        isSettingMenuOpen.value = true
    }

    fun onUseGraphSqliteExposedPressed(state: MyWindowState, graph: Graph) {
        state.reloadWindow(graph, scope)
    }

    fun onDeleteGraphSqliteExposedPressed(id: Int) {
        deleteGraph(id)
        graphList.value = graphList.value.filter { it.first != id }
    }

    fun generateGraph(maxWeight: Int): Graph {
        val graphSize = graphSize.value.toInt()
        return when (chosenGenerator.value) {
            "Random Tree" -> randomTree(graphSize, maxWeight)
            "Flower Snark" -> flowerSnark(graphSize)
            "Star Directed" -> starDirected(graphSize)
            "Star Undirected" -> starUndirected(graphSize)
            else -> createEmptyGraph()
        }
    }

    fun createEmptyGraph(): Graph {
        return Graph()
    }

    fun createGraphWithoutEdges(): Graph {
        val size = graphSize.value.toInt()

        val graph = Graph()
        for (i in 0 until size) {
            graph.addNode(i.toString())
        }
        return graph
    }
}