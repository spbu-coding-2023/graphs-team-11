package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.deleteGraph
import data.db.sqlite_exposed.getAllGraphs
import data.tools.graphGenerators.flowerSnark
import data.tools.graphGenerators.randomTree
import data.tools.graphGenerators.starDirected
import data.tools.graphGenerators.starUndirected
import model.graph_model.Graph
import ui.components.MyWindowState
import ui.components.generateStringNodeNames

class IntroWindowVM(
    var isSettingMenuOpen: MutableState<Boolean>,
) {
    var graphList: MutableState<List<Triple<Int, Graph<*>, String>>>

    init {
        connect()
        graphList = mutableStateOf(getAllGraphs())
    }

    enum class GraphKeyType {
        INT, STRING, FLOAT, CHAR,
    }

    fun onSettingsPressed() {
        isSettingMenuOpen.value = true
    }

    fun onUseGraphSqliteExposedPressed(state: MyWindowState, graph: Graph<*>) {
        state.close()
        state.openNewWindow(graph)
    }

    fun onDeleteGraphSqliteExposedPressed(id: Int, graphList: MutableState<List<Triple<Int, Graph<*>, String>>>) {
        deleteGraph(id)
        graphList.value = graphList.value.filter { it.first != id }
    }

    fun generateGraph(graphSize: Int, chosenGenerator: String, graphType: GraphKeyType): Graph<*> {
        return when (chosenGenerator) {
            "Random Tree" -> randomTree(graphSize, graphType)
            "Flower Snark" -> flowerSnark(graphSize, graphType)
            "Star Directed" -> starDirected(graphSize, graphType)
            "Star Undirected" -> starUndirected(graphSize, graphType)
            else -> createEmptyGraph(graphType)
        }
    }

    fun createEmptyGraph(type: GraphKeyType): Graph<*> {
        return when (type) {
            GraphKeyType.INT -> Graph<Int>()
            GraphKeyType.STRING -> Graph<String>()
            GraphKeyType.FLOAT -> Graph<Float>()
            GraphKeyType.CHAR -> Graph<Char>()
        }
    }

    fun createGraphWithoutEdges(type: GraphKeyType, size: Int): Graph<*> {
        return when (type) {
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

            GraphKeyType.CHAR -> {
                val graph = Graph<Char>()
                for (i in 0 until size) {
                    graph.addNode('A' + i)
                }
                graph
            }
        }

    }

}