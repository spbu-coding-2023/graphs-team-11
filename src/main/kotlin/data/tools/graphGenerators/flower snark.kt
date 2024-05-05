package data.tools.graphGenerators

import data.Graph

fun flowerSnark(n: Int): Graph<Int> {
    val graph = Graph<Int>()
    for (i in 0..<n) {
        graph.addNode(i * 4)
        for (j in 1..3) {
            graph.addNode(i * 4 + j)
            graph.addVertice(i * 4 + j, i * 4)
            graph.addVertice(i * 4, i * 4 + j)
        }
    }

    for (i in 0..<(n - 1)) {
        graph.addVertice(i * 4 + 1, i * 4 + 5)
        graph.addVertice(i * 4 + 5, i * 4 + 1)
    }
    graph.addVertice(4 * n - 3, 1)
    graph.addVertice(1, 4 * n - 3)

    for (i in 0..<(n - 1)) {
        graph.addVertice(i * 4 + 2, i * 4 + 6)
        graph.addVertice(i * 4 + 6, i * 4 + 2)
    }
    graph.addVertice(4 * n - 2, 3)
    graph.addVertice(3, 4 * n - 2)

    for (i in 0..<(n - 1)) {
        graph.addVertice(i * 4 + 3, i * 4 + 7)
        graph.addVertice(i * 4 + 7, i * 4 + 3)
    }
    graph.addVertice(4 * n - 1, 2)
    graph.addVertice(2, 4 * n - 1)

    return graph
}