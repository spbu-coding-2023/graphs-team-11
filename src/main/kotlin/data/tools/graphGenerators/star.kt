package data.tools.graphGenerators

import model.graph_model.Graph

fun starDirected(n: Int): Graph<Int> {
    val graph = Graph<Int>()

    graph.addNode(0)

    for (i in 1..n) {
        graph.addNode(i)
        graph.addVertice(0, i)
    }

    return graph
}

fun starUndirected(n: Int): Graph<Int> {
    val graph = Graph<Int>()

    graph.addNode(0)

    for (i in 1..n) {
        graph.addNode(i)
        graph.addVertice(0, i)
        graph.addVertice(i, 0)
    }
    return graph
}