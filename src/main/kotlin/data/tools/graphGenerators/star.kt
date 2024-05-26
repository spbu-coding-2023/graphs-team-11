package data.tools.graphGenerators

import model.graph_model.Graph
import model.graph_model.UndirectedGraph
import ui.components.GraphKeyType
import ui.components.generateStringNodeNames

fun starDirected(n: Int, type: GraphKeyType): Graph<*> = when (type) {
    GraphKeyType.INT -> starDirectedInt(n)
    GraphKeyType.STRING -> starDirectedString(n)
    GraphKeyType.FLOAT -> starDirectedFloat(n)
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

fun starUndirected(n: Int, type: GraphKeyType): Graph<*> = when (type) {
    GraphKeyType.INT -> starUndirectedInt(n)
    GraphKeyType.STRING -> starUndirectedString(n)
    GraphKeyType.FLOAT -> starUndirectedFloat(n)
}

private fun starUndirectedInt(n: Int): UndirectedGraph<Int> {
    val graph = UndirectedGraph<Int>()
    graph.addNode(1)
    for (i in 2..n) {
        graph.addNode(i)
        graph.addVertice(1, i)
    }
    return graph
}

private fun starUndirectedString(n: Int): UndirectedGraph<String> {
    val graph = UndirectedGraph<String>()
    val nodeNames = generateStringNodeNames(n)
    graph.addNode(nodeNames[0])
    for (i in 1..<n) {
        graph.addNode(nodeNames[i])
        graph.addVertice(nodeNames[0], nodeNames[i])
    }
    return graph
}

private fun starUndirectedFloat(n: Int): UndirectedGraph<Float> {
    val graph = UndirectedGraph<Float>()
    graph.addNode(1f)
    for (i in 2..n) {
        graph.addNode(i.toFloat())
        graph.addVertice(1f, i.toFloat())
    }
    return graph
}
