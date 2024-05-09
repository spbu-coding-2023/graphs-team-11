package model.algoritms

import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

interface Algoritm {
    open fun <D> alogRun(graph: Graph<D>): Update<D> {
        return Update()
    }
}