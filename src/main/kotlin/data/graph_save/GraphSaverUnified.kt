package data.graph_save

import model.graph_model.Graph
import java.nio.file.Path

fun graphSaveUnified(path: String, graph: Graph<*>) {
    when {
        path.endsWith(".graphml") -> saveGraphML(path, graph)
    }
}