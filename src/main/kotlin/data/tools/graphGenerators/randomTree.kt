package data.tools.graphGenerators

import model.graph_model.Graph
import ui.components.generateStringNodeNames
import viewmodel.IntroWindowVM
import kotlin.random.Random

fun randomTree(n: Int, type: IntroWindowVM.GraphKeyType, maxWeight: Int): Graph<*> = when (type) {
    IntroWindowVM.GraphKeyType.INT -> randomTreeInt(n, maxWeight)
    IntroWindowVM.GraphKeyType.STRING -> randomTreeString(n, maxWeight)
    IntroWindowVM.GraphKeyType.FLOAT -> randomTreeFloat(n, maxWeight)
    IntroWindowVM.GraphKeyType.CHAR -> randomTreeChar(n, maxWeight)
}


private fun randomTreeInt(n: Int, maxWeight: Int): Graph<Int> {
    val graph = Graph<Int>()

    graph.addNode(1)
    graph.addNode(2)
    graph.addVertice(1, 2)

    if (maxWeight > 1) {
        for (i in 3..n) {
            graph.addNode(i)
            graph.addVertice(Random.nextInt(1, i - 1), i, Random.nextInt(1, maxWeight).toFloat())
        }
    } else {
        for (i in 3..n) {
            graph.addNode(i)
            graph.addVertice(Random.nextInt(1, i - 1), i)
        }
    }

    return graph
}

private fun randomTreeString(n: Int, maxWeight: Int): Graph<String> {
    val graph = Graph<String>()
    val nodeNames = generateStringNodeNames(n)

    graph.addNode(nodeNames[0])
    graph.addNode(nodeNames[1])
    if (maxWeight > 1) {
        graph.addVertice(nodeNames[0], nodeNames[1], Random.nextInt(1, maxWeight).toFloat())
    } else {
        graph.addVertice(nodeNames[0], nodeNames[1])
    }

    for (i in 2..<n) {
        graph.addNode(nodeNames[i])

    }

    if (maxWeight > 1) {
        for (i in 2..<n) {
            graph.addVertice(nodeNames[Random.nextInt(0, i - 1)], nodeNames[i], Random.nextInt(1, maxWeight).toFloat())
        }
    } else {
        for (i in 2..<n) {
            graph.addVertice(nodeNames[Random.nextInt(0, i - 1)], nodeNames[i])
        }
    }

    return graph
}

private fun randomTreeFloat(n: Int, maxWeight: Int): Graph<Float> {
    val graph = Graph<Float>()

    graph.addNode(1f)
    graph.addNode(2f)
    if (maxWeight > 1) {
        graph.addVertice(1f, 2f, Random.nextInt(1, maxWeight).toFloat())

        for (i in 3..n) {
            graph.addNode(i.toFloat())
            graph.addVertice(Random.nextInt(1, i - 1).toFloat(), i.toFloat(), Random.nextInt(1, maxWeight).toFloat())
        }
    } else {
        graph.addVertice(1f, 2f)

        for (i in 3..n) {
            graph.addNode(i.toFloat())
            graph.addVertice(Random.nextInt(1, i - 1).toFloat(), i.toFloat())
        }
    }

    return graph
}

private fun randomTreeChar(n: Int, maxWeight: Int): Graph<Char> {
    val graph = Graph<Char>()

    graph.addNode('A')
    graph.addNode('B')
    if (maxWeight > 1) {
        graph.addVertice('A', 'B', Random.nextInt(1, maxWeight).toFloat())
    } else {
        graph.addVertice('A', 'B')
    }

    for (i in 2..<n) {
        graph.addNode('A' + i)
    }

    if (maxWeight > 1) {
        for (i in 2..<n) {
            graph.addVertice('A' + Random.nextInt(0, i - 1), 'A' + i, Random.nextInt(1, maxWeight).toFloat())
        }
    } else {
        for (i in 2..<n) {
            graph.addVertice('A' + Random.nextInt(0, i - 1), 'A' + i)
        }
    }

    return graph
}
