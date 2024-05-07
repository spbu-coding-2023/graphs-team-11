package algorithm.main

import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

abstract class Algoritm {
    open fun <D> alogRun(graph: Graph<D>): Update<D> {
        return Update()
    }
}