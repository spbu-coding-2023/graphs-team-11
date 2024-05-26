package data.tools.graphGenerators

import model.graph_model.Graph
import model.graph_model.UndirectedGraph

fun starDirected(n: Int): Graph {
    val graph = Graph()
    graph.addNode("1")
    for (i in 2..n) {
        val curr = i.toString()
        graph.addNode(curr)
        graph.addVertice("1", curr)
    }
    return graph
}

fun starUndirected(n: Int): Graph {
    val graph = UndirectedGraph()
    graph.addNode("1")
    for (i in 2..n) {
        val curr = i.toString()
        graph.addNode(curr)
        graph.addVertice("1", curr)
    }
    return graph
}
