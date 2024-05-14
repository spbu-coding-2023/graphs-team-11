package data.tools.graphGenerators

import model.graph_model.Graph
import ui.components.generateStringNodeNames
import viewmodel.IntroWindowVM
import kotlin.random.Random

fun randomTree(n: Int, type: IntroWindowVM.GraphKeyType): Graph<*> = when (type) {
    IntroWindowVM.GraphKeyType.INT -> randomTreeInt(n)
    IntroWindowVM.GraphKeyType.STRING -> randomTreeString(n)
    IntroWindowVM.GraphKeyType.FLOAT -> randomTreeFloat(n)
    IntroWindowVM.GraphKeyType.CHAR -> randomTreeChar(n)
}


private fun randomTreeInt(n: Int): Graph<Int> {
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

private fun randomTreeString(n: Int): Graph<String> {
    val graph = Graph<String>()
    val nodeNames = generateStringNodeNames(n)

    graph.addNode(nodeNames[0])
    graph.addNode(nodeNames[1])
    graph.addVertice(nodeNames[0], nodeNames[1])

    for (i in 2..<n) {
        graph.addNode(nodeNames[i])

    }

    for (i in 2..<n) {
        graph.addVertice(nodeNames[Random.nextInt(0, i - 1)], nodeNames[i])
    }

    return graph
}

private fun randomTreeFloat(n: Int): Graph<Float> {
    val graph = Graph<Float>()

    graph.addNode(0f)
    graph.addNode(1f)
    graph.addVertice(0f, 1f)

    for (i in 2..n) {
        graph.addNode(i.toFloat())
        graph.addVertice(Random.nextFloat() * (i - 1), i.toFloat())
    }

    return graph
}

private fun randomTreeChar(n: Int): Graph<Char> {
    val graph = Graph<Char>()

    graph.addNode('A')
    graph.addNode('B')
    graph.addVertice('A', 'B')

    for (i in 2..<n) {
        graph.addNode('A' + i)
    }

    for (i in 2..<n) {
        graph.addVertice('A' + Random.nextInt(0, i - 1), 'A' + i)
    }

    return graph
}
