package data.graph_save

import model.graph_model.Graph

fun graphLoadUnified(path: String): Graph {

    var graph = Graph()
    try {
        when {
            path.endsWith(".graphml") -> {
                graph = loadGraphML(path)
            }
        }
    } catch (e: NullPointerException) {
        throw e
    }

    return graph
}
