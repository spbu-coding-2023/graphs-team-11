package data.tools.graphGenerators

import model.graph_model.Graph
import ui.components.generateStringNodeNames
import viewmodel.IntroWindowVM

fun starDirected(n: Int, type: IntroWindowVM.GraphKeyType): Graph<*> = when (type) {
    IntroWindowVM.GraphKeyType.INT -> starDirectedInt(n)
    IntroWindowVM.GraphKeyType.STRING -> starDirectedString(n)
    IntroWindowVM.GraphKeyType.FLOAT -> starDirectedFloat(n)
    IntroWindowVM.GraphKeyType.CHAR -> starDirectedChar(n)
}

private fun starDirectedInt(n: Int): Graph<Int> {
    val graph = Graph<Int>()
    graph.addNode(1)
    for (i in 2..n) {
        graph.addNode(i)
        graph.addVertice(1, i)
    }
    return graph
}

private fun starDirectedString(n: Int): Graph<String> {
    val graph = Graph<String>()
    val nodeNames = generateStringNodeNames(n)
    graph.addNode(nodeNames[0])
    for (i in 1..<n) {
        graph.addNode(nodeNames[i])
        graph.addVertice(nodeNames[0], nodeNames[i])
    }
    return graph
}

private fun starDirectedFloat(n: Int): Graph<Float> {
    val graph = Graph<Float>()
    graph.addNode(1f)
    for (i in 2..n) {
        graph.addNode(i.toFloat())
        graph.addVertice(1f, i.toFloat())
    }
    return graph
}

private fun starDirectedChar(n: Int): Graph<Char> {
    val graph = Graph<Char>()
    graph.addNode('A')
    for (i in 1..<n) {
        graph.addNode('A' + i)
        graph.addVertice('A', 'A' + i)
    }
    return graph
}

fun starUndirected(n: Int, type: IntroWindowVM.GraphKeyType): Graph<*> = when (type) {
    IntroWindowVM.GraphKeyType.INT -> starUndirectedInt(n)
    IntroWindowVM.GraphKeyType.STRING -> starUndirectedString(n)
    IntroWindowVM.GraphKeyType.FLOAT -> starUndirectedFloat(n)
    IntroWindowVM.GraphKeyType.CHAR -> starUndirectedChar(n)
}

private fun starUndirectedInt(n: Int): Graph<Int> {
    val graph = Graph<Int>()
    graph.addNode(1)
    for (i in 2..n) {
        graph.addNode(i)
        graph.addVertice(1, i)
        graph.addVertice(i, 1)
    }
    return graph
}

private fun starUndirectedString(n: Int): Graph<String> {
    val graph = Graph<String>()
    val nodeNames = generateStringNodeNames(n)
    graph.addNode(nodeNames[0])
    for (i in 1..<n) {
        graph.addNode(nodeNames[i])
        graph.addVertice(nodeNames[0], nodeNames[i])
        graph.addVertice(nodeNames[i], nodeNames[0])
    }
    return graph
}

private fun starUndirectedFloat(n: Int): Graph<Float> {
    val graph = Graph<Float>()
    graph.addNode(1f)
    for (i in 2..n) {
        graph.addNode(i.toFloat())
        graph.addVertice(1f, i.toFloat())
        graph.addVertice(i.toFloat(), 1f)
    }
    return graph
}

private fun starUndirectedChar(n: Int): Graph<Char> {
    val graph = Graph<Char>()
    graph.addNode('A')
    for (i in 1..<n) {
        graph.addNode('A' + i)
        graph.addVertice('A', 'A' + i)
        graph.addVertice('A' + i, 'A')
    }
    return graph
}