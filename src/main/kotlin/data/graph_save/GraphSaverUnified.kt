package data.graph_save

import model.graph_model.Graph


fun graphSaveUnified(path: String, graph: Graph) {
    when {
        path.endsWith(".graphml") -> saveGraphML(path, graph)
    }
}