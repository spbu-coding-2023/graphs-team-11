package viewmodel.cosmetics

import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update

class ColorSetter : CosmeticWidgetsMV {

    var color: Color = Color.White
    override fun getUpdate(graph: Graph): Update {
        val nodeViewUpdate: MutableMap<String, NodeViewUpdate> = mutableMapOf()

        for ((node, _) in graph.vertices) {
            nodeViewUpdate[node] = NodeViewUpdate(color = this.color)
        }
        return Update(nodeViewUpdate = nodeViewUpdate)
    }

}