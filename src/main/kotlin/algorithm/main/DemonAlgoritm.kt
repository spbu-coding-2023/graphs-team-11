package algorithm.main

import data.Graph
import ui.graph_view.graph_view_actions.NodeViewUpdate
import ui.graph_view.graph_view_actions.Update

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