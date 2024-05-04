package data.algoritms

import androidx.compose.ui.graphics.Color
import data.Graph
import data.algoritms.Algoritm
import ui.graph_view.graph_view_actions.NodeViewUpdate

class SampleAlgo: Algoritm() {
    override fun <D> alogRun(graph: Graph<D>): MutableMap<D, NodeViewUpdate<D>> {
        var update: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        for (i in graph.vertices) {
            if (i.value.size % 2 == 1) {
                update[i.key] = NodeViewUpdate(color = Color.Red, radius = i.value.size.toFloat() * 5)
            } else {
                update[i.key] = NodeViewUpdate(color = Color.Green, radius = i.value.size.toFloat() * 5)
            }
        }
        return update
    }
}