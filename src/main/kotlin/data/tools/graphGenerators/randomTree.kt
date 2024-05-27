package data.tools.graphGenerators

import model.graph_model.Graph
import model.graph_model.UndirectedGraph
import kotlin.random.Random

fun randomTree(n: Int, maxWeight: Int, isDirected: Boolean = true): Graph {

    val graph = if (isDirected) Graph() else UndirectedGraph()

    graph.addNode("1")
    graph.addNode("2")
    graph.addVertice("1", "2")

    if (maxWeight > 1) {
        for (i in 3..n) {
            graph.addNode("$i")
            graph.addVertice(Random.nextInt(1, i - 1).toString(), i.toString(), Random.nextInt(1, maxWeight).toFloat())
        }
    } else {
        for (i in 3..n) {
            graph.addNode(i.toString())
            graph.addVertice(Random.nextInt(1, i - 1).toString(), i.toString())
        }
    }

    return graph
}
