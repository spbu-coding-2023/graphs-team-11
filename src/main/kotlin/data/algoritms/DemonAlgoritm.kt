package data.algoritms

import data.Graph
import ui.graph_view.graph_view_actions.NodeViewUpdate

abstract class DemonAlgoritm: Algoritm() {
    override fun <D> alogRun(graph: Graph<D>): MutableMap<D, NodeViewUpdate<D>> {
        TODO("Demon Run")
    }
}

class Floyd: DemonAlgoritm() {
    override fun <D> alogRun(graph: Graph<D>): MutableMap<D, NodeViewUpdate<D>> {
        TODO("Floyd Run")
    }
}