package components.graph_view

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import components.graph_view.graph_view_actions.NodeViewUpdate
import data.Graph
import kotlin.math.sqrt
import kotlin.random.Random

data class NodeView<D>(var offset: Offset, var radius: Float, var color: Color, var value: D, var shape: Shape, var alpha: Float = 1f)
data class VertView<D>(var start: NodeView<D>, var end: NodeView<D>, var color: Color, var alpha: Float = 1f)

class GrahpView<D>(var graph: Graph<D>,
                   var radius: Float = 30f,
                   var nodeColor: Color = Color.Blue,
                   var vertColor: Color = Color.Red,
                   var baseShape: Shape = CircleShape,
                   var nodesViews: MutableMap<D, NodeView<D>> =  mutableMapOf(),
                   var vertViews: MutableList<VertView<D>> = mutableListOf()) {

    init {
        var positions = FA2Layout()
        for (i in positions) {
            nodesViews[i.key] = NodeView(
                offset = positions[i.key]!!,
                radius = radius,
                color = nodeColor,
                value = i.key,
                shape = baseShape)
        }
        for (i in graph.vertices) {
            for (j in i.value) {
                vertViews.add(
                    VertView(start = nodesViews[i.key]!!,
                    end = nodesViews[j.first]!!,
                    color = vertColor,
                        alpha = 0.5f)
                )
            }
        }
    }
    fun applyAction(action: MutableMap<D, NodeViewUpdate<D>>) {
        TODO("Create GraphView Action Interface")
    }

    // just for fast not implemented like algoritm
    fun FA2Layout(eps: Float = 1.0E-20F, iterations: Int = 100, speed: Float = 0.1f, speedEfficiency: Float = 1f): MutableMap<D, Offset> {
        var positions: MutableMap<D, Offset> = mutableMapOf()

        for (i in this.graph.vertices) {
            // Create different window size
            positions[i.key] = Offset(x = Random.nextFloat() * 1000, y = Random.nextFloat() * 800)
        }

        return positions
    }
}