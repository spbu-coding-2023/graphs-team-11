package data.tools.graphGenerators

import model.graph_model.Graph
import ui.components.generateStringNodeNames
import viewmodel.IntroWindowVM

fun flowerSnark(n: Int, type: IntroWindowVM.GraphKeyType): Graph<*> = when (type) {
    IntroWindowVM.GraphKeyType.INT -> flowerSnarkInt(n)
    IntroWindowVM.GraphKeyType.STRING -> flowerSnarkString(n)
    IntroWindowVM.GraphKeyType.FLOAT -> flowerSnarkFloat(n)
}

private fun flowerSnarkInt(n: Int): Graph<Int> {
    val graph = Graph<Int>()
    for (i in 0 until n) {
        graph.addNode(i * 4)
        for (j in 1..3) {
            graph.addNode(i * 4 + j)
            graph.addVertice(i * 4 + j, i * 4)
        }
    }

    for (i in 0 until n - 1) {
        graph.addVertice(i * 4 + 1, i * 4 + 5)
    }
    graph.addVertice(4 * n - 3, 1)

    for (i in 0 until n - 1) {
        graph.addVertice(i * 4 + 2, i * 4 + 6)
    }
    graph.addVertice(4 * n - 2, 2)

    for (i in 0 until n - 1) {
        graph.addVertice(i * 4 + 3, i * 4 + 7)
    }
    graph.addVertice(4 * n - 1, 3)

    return graph
}

private fun flowerSnarkString(n: Int): Graph<String> {
    val graph = Graph<String>()
    val nodeNames = generateStringNodeNames(n * 4)

    for (i in 0 until n) {
        graph.addNode(nodeNames[i * 4])
        for (j in 1..3) {
            graph.addNode(nodeNames[i * 4 + j])
            graph.addVertice(nodeNames[i * 4 + j], nodeNames[i * 4])
        }
    }

    for (i in 0 until n - 1) {
        graph.addVertice(nodeNames[i * 4 + 1], nodeNames[i * 4 + 5])
    }
    graph.addVertice(nodeNames[4 * n - 3], nodeNames[1])

    for (i in 0 until n - 1) {
        graph.addVertice(nodeNames[i * 4 + 2], nodeNames[i * 4 + 6])
    }
    graph.addVertice(nodeNames[4 * n - 2], nodeNames[2])

    for (i in 0 until n - 1) {
        graph.addVertice(nodeNames[i * 4 + 3], nodeNames[i * 4 + 7])
    }
    graph.addVertice(nodeNames[4 * n - 1], nodeNames[3])

    return graph
}

private fun flowerSnarkFloat(n: Int): Graph<Float> {
    val graph = Graph<Float>()
    for (i in 0 until n) {
        graph.addNode(i * 4f)
        for (j in 1..3) {
            graph.addNode(i * 4f + j)
            graph.addVertice(i * 4f + j, i * 4f)
        }
    }

    for (i in 0 until n - 1) {
        graph.addVertice(i * 4f + 1f, i * 4f + 5f)
    }
    graph.addVertice(4 * n - 3f, 1f)

    for (i in 0 until n - 1) {
        graph.addVertice(i * 4f + 2f, i * 4f + 6f)
    }
    graph.addVertice(4 * n - 2f, 2f)

    for (i in 0 until n - 1) {
        graph.addVertice(i * 4f + 3f, i * 4f + 7f)
    }
    graph.addVertice(4 * n - 1f, 3f)

    return graph
}
