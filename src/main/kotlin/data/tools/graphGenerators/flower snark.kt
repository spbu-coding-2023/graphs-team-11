package data.tools.graphGenerators

import data.Graph

fun flowerSnark(n: Int): Graph<Int> {
    val graph = Graph<Int>()
    for (i in 0..<n) {
        graph.addNode(i * 4)
        for (j in 1..3) {
            graph.addNode(i * 4 + j)
            graph.addVertice(i * 4 + j, i * 4)
        }
    }

    for (i in 0..<(n - 1)) {
        graph.addVertice(i * 4 + 1, i * 4 + 5)
    }
    graph.addVertice(4 * n - 3, 1)

    for (i in 0..<(n - 1)) {
        graph.addVertice(i * 4 + 2, i * 4 + 6)
    }
    graph.addVertice(4 * n - 2, 3)

    for (i in 0..<(n - 1)) {
        graph.addVertice(i * 4 + 3, i * 4 + 7)
    }
    graph.addVertice(4 * n - 1, 2)

    return graph
}