package data.tools.graphGenerators

import data.Graph
import kotlin.random.Random

fun randomTree(n: Int): Graph<Int> {
    val graph = Graph<Int>()

    graph.addNode(0)
    graph.addNode(1)
    graph.addVertice(0, 1)

    for (i in 2..n) {
        graph.addNode(i)
        graph.addVertice(Random.nextInt(0, i - 1), i)
    }

    return graph
}