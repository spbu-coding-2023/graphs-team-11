package algorithm.main

import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate

class SampleAlgo : Algoritm() {
    override fun <D> alogRun(graph: Graph<D>): Update<D> {
        val updateNode: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        val updateVert: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()
        for (i in graph.vertices) {
            if (i.value.size % 2 == 1) {
                updateNode[i.key] = NodeViewUpdate(color = Color.Red, radius = i.value.size.toFloat() * 5 + 5)
            } else {
                updateNode[i.key] = NodeViewUpdate(color = Color.Green, radius = i.value.size.toFloat() * 5 + 5)
            }
            updateVert[i.key] = mutableMapOf()
            for (j in i.value) {
                updateVert[i.key]!!.set(j.first, VertViewUpdate<D>(color = Color.Yellow))
            }
        }
        return Update<D>(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }
}