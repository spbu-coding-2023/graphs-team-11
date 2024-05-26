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
import ui.components.GraphKeyType
import ui.components.MyWindowState
import ui.components.generateStringNodeNames

class IntroWindowVM(
    var isSettingMenuOpen: MutableState<Boolean>, private val scope: CoroutineScope, graphKeyType: GraphKeyType
) {
    var graphList: MutableState<List<Triple<Int, Graph<*>, String>>> = mutableStateOf(getAllGraphs())
    val isFileLoaderOpen = mutableStateOf(false)
    val fileLoaderException: MutableState<String?> = mutableStateOf(null)
    val expanded = mutableStateOf(false)
    val chosenGraph = mutableStateOf("Saved")
    val graphSize = mutableStateOf("")
    val chosenGenerator = mutableStateOf("Random Tree")
    val weightMax = mutableStateOf("1")
    val selectedGraphKeyType = mutableStateOf(graphKeyType)


    fun onSettingsPressed() {
        isSettingMenuOpen.value = true
    }

    fun onUseGraphSqliteExposedPressed(state: MyWindowState, graph: Graph<*>) {
        state.reloadWindow(graph, scope, graphKeyType = selectedGraphKeyType.value)
    }

    fun onDeleteGraphSqliteExposedPressed(id: Int) {
        deleteGraph(id)
        graphList.value = graphList.value.filter { it.first != id }
    }

    fun generateGraph(maxWeight: Int): Graph<*> {
        val graphSize = graphSize.value.toInt()
        return when (chosenGenerator.value) {
            "Random Tree" -> randomTree(graphSize, selectedGraphKeyType.value, maxWeight)
            "Flower Snark" -> flowerSnark(graphSize, selectedGraphKeyType.value)
            "Star Directed" -> starDirected(graphSize, selectedGraphKeyType.value)
            "Star Undirected" -> starUndirected(graphSize, selectedGraphKeyType.value)
            else -> createEmptyGraph()
        }
    }

    fun createEmptyGraph(): Graph<*> {
        return when (selectedGraphKeyType.value) {
            GraphKeyType.INT -> Graph<Int>()
            GraphKeyType.STRING -> Graph<String>()
            GraphKeyType.FLOAT -> Graph<Float>()
        }
    }

    fun createGraphWithoutEdges(): Graph<*> {
        val size = graphSize.value.toInt()
        return when (selectedGraphKeyType.value) {
            GraphKeyType.INT -> {
                val graph = Graph<Int>()
                for (i in 0 until size) {
                    graph.addNode(i)
                }
                graph

            }

            GraphKeyType.STRING -> {
                val graph = Graph<String>()
                val nodeNames = generateStringNodeNames(size)
                nodeNames.forEach { nodeName ->
                    graph.addNode(nodeName)
                }
                graph
            }

            GraphKeyType.FLOAT -> {
                val graph = Graph<Float>()
                for (i in 0 until size) {
                    graph.addNode(i.toFloat())
                }
                graph
            }
        }

    }
}