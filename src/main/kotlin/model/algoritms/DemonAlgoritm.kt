package model.algoritms

import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

abstract class DemonAlgoritm : Algoritm() {
    override fun <D> alogRun(graph: Graph<D>): Update<D> {
        TODO("Demon Run")
    }
}

class Floyd : DemonAlgoritm() {
    override fun <D> alogRun(graph: Graph<D>): Update<D> {
        TODO("Floyd Run")
    }
}