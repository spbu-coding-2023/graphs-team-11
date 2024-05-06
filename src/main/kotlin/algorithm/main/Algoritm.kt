package algorithm.main

import data.Graph
import ui.graph_view.graph_view_actions.NodeViewUpdate
import ui.graph_view.graph_view_actions.Update

abstract class Algoritm {
    open fun <D> alogRun(graph: Graph<D>): Update<D> {
        return Update()
    }
}