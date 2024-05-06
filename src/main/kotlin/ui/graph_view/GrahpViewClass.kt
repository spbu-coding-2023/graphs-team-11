package ui.graph_view

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import ui.graph_view.graph_view_actions.NodeViewUpdate
import data.Graph
import ui.graph_view.graph_view_actions.Update
import ui.graph_view.graph_view_actions.VertViewUpdate
import java.util.Stack
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

data class NodeView<D>(
    var offset: Offset, var radius: Float, var color: Color, var value: D, var shape: Shape, var alpha: Float = 1f
)

data class VertView<D>(var start: NodeView<D>, var end: NodeView<D>, var color: Color, var alpha: Float = 1f)

class GrahpViewClass<D>(
    var graph: Graph<D>,
    var radius: Float = 10f,
    var nodeColor: Color = Color.Blue,
    var vertColor: Color = Color.Red,
    var baseShape: Shape = CircleShape,
    nodesViews: MutableMap<D, NodeView<D>> = mutableMapOf(),
    vertViews: MutableMap<D, MutableMap<D, VertView<D>>> = mutableMapOf()
) {

    var nodesViews by mutableStateOf(nodesViews)
        private set
    var vertViews by mutableStateOf(vertViews)
        private set

    var returnStack by mutableStateOf(Stack<Update<D>>())

    init {
        val positions = FA2Layout()
        for (i in positions) {
            nodesViews[i.key] = NodeView(
                offset = positions[i.key]!!, radius = radius, color = nodeColor, value = i.key, shape = baseShape
            )
        }
        for ((i, verts) in graph.vertices) {
            vertViews[i] = mutableMapOf()
            for ((j, weight) in verts) {
                vertViews[i]!!.set(j, VertView(
                    start = nodesViews[i]!!, end = nodesViews[j]!!, color = vertColor, alpha = 0.5f
                )
                )
            }
        }
    }

    fun applyUpdate(update: Update<D>, isNotReUpdate: Boolean = true) {
        val nodeViewReUpdate: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        val vertViewReUpdate: MutableMap<D, MutableMap<D, VertViewUpdate<D>>> = mutableMapOf()
        for (v in update.nodeViewUpdate) {
            if (isNotReUpdate) nodeViewReUpdate[v.key] = NodeViewUpdate(
                offset = nodesViews[v.key]!!.offset,
                radius = nodesViews[v.key]!!.radius,
                color = nodesViews[v.key]!!.color,
                shape = nodesViews[v.key]!!.shape,
                alpha = nodesViews[v.key]!!.alpha
            )
            if (v.value.offset != null) nodesViews[v.key]!!.offset = v.value.offset!!
            if (v.value.radius != null) nodesViews[v.key]!!.radius = v.value.radius!!
            if (v.value.color != null) nodesViews[v.key]!!.color = v.value.color!!
            if (v.value.shape != null) nodesViews[v.key]!!.shape = v.value.shape!!
            if (v.value.alpha != null) nodesViews[v.key]!!.alpha = v.value.alpha!!
        }
        for ((v, verts) in update.vertViewUpdate) {
            vertViewReUpdate[v] = mutableMapOf()
            for ((u, viewUpdate) in verts) {
                if (isNotReUpdate) vertViewReUpdate[v]!![u] = VertViewUpdate(
                    color = vertViews[v]!![u]!!.color,
                    alpha = vertViews[v]!![u]!!.alpha
                )
                if (viewUpdate.color != null) vertViews[v]!![u]!!.color = viewUpdate.color
                if (viewUpdate.alpha != null) vertViews[v]!![u]!!.alpha = viewUpdate.alpha
            }
        }
        if (isNotReUpdate) returnStack.add(Update(nodeViewUpdate = nodeViewReUpdate, vertViewUpdate = vertViewReUpdate))
    }

    fun comeBack() {
        println(returnStack)
        if (this.returnStack.size > 0) this.applyUpdate(this.returnStack.pop(), isNotReUpdate = false)
    }

    // just for fast not implemented like algoritm
    private fun FA2Layout(iterations: Int = 2): MutableMap<D, Offset> {
        val positions: MutableMap<D, Offset> = mutableMapOf()

        var t = 0.4

        val C = 1 / sqrt(graph.vertices.size.toFloat())
        val k = C * sqrt(4f / graph.vertices.size)

        val atr = { x: Float, y: Float -> -C * y * k * k / x }
        val retr = { x: Float, y: Float, z: Float -> ((x - k) / y) - atr(x, z) }

        for (i in this.graph.vertices) {
            // Create different window size
            positions[i.key] = Offset(x = 1 - 2 * Random.nextFloat(), y = 1 - 2 * Random.nextFloat())
        }

        val oldPos: MutableMap<D, Offset> = mutableMapOf()

        var convered = 0

        while (convered != 1) {
            convered = 1

            for (u in graph.vertices) oldPos[u.key] = positions[u.key]!!

            for (v in graph.vertices) {
                var disp = Offset(x = 0f, y = 0f)
                for (u in graph.vertices) {
                    if (u != v) {
                        val delta = positions[u.key]!! - positions[v.key]!!
                        disp += delta / abs(delta) * atr(abs(delta), abs(positions[u.key]!!))
                    }
                }
                for (u in v.value) {
                    val delta = positions[u.first]!! - positions[v.key]!!
                    disp += delta / abs(delta) * retr(
                        abs(delta), v.value.size.toFloat(), abs(positions[u.first]!!)
                    )
                }

                if (!abs(disp).isNaN()) {
                    positions[v.key] = positions[v.key]!! + disp / abs(disp) * min(abs(disp), t.toFloat())

                    positions[v.key] = Offset(
                        x = min(1f, max(-1f, positions[v.key]!!.x)), y = min(1f, max(-1f, positions[v.key]!!.y))
                    )
                }

                val delta = positions[v.key]!! - oldPos[v.key]!!
                if (abs(delta) > k * t) convered = 0
            }

            t *= (1 - 1 / iterations)
        }
        var xmax = 0f
        var xmin = 2f
        var ymax = 0f
        var ymin = 2f
        for (v in positions) {
            xmax = max(xmax, 1 + v.value.x)
            xmin = min(xmin, 1 + v.value.x)
            ymax = max(ymax, 1 + v.value.y)
            ymin = min(ymin, 1 + v.value.y)
        }
        println(Pair(xmin, xmax))
        println(Pair(ymin, ymax))

        val xdelta = xmax - xmin
        val ydelta = ymax - ymin

        for (v in positions) {
            positions[v.key] = Offset(
                x = (positions[v.key]!!.x - xmin + 1) * 2 / xdelta - 1,
                y = (positions[v.key]!!.y - ymin + 1) * 2 / ydelta - 1
            )
        }

        return positions
    }
}

fun abs(offset: Offset): Float {
    val t = sqrt(offset.x * offset.x + offset.y * offset.y)
    return t
}
