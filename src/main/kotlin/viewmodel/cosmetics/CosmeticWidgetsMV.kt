package viewmodel.cosmetics

import model.graph_model.Graph
import model.graph_model.graph_model_actions.Update

interface CosmeticWidgetsMV {
    fun getUpdate(graph: Graph): Update
}