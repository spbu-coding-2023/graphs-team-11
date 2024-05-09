package viewmodel.cosmetics

import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update

class ColorSetter: CosmeticWidgetsMV {

    var color: Color = Color.White
    override fun <D> getUpdate(graph: Graph<D>): Update<D> {
        val nodeViewUpdate: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()

        for ((node, _) in graph.vertices) {
            nodeViewUpdate[node] = NodeViewUpdate(color = this.color)
        }
        return Update(nodeViewUpdate = nodeViewUpdate)
    }

}