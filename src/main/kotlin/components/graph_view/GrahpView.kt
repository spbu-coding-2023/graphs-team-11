package components.graph_view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import components.graph_view.graph_view_actions.GraphAction
import data.Graph
import kotlin.math.sqrt
import kotlin.random.Random

data class NodeView<D>(var offset: Offset, var radius: Float, var color: Color, var value: D)
data class VertView<D>(var start: NodeView<D>, var end: NodeView<D>, var color: Color)

class GrahpView<D>(var graph: Graph<D>,
                   var radius: Float = 10f,
                   var nodeColor: Color = Color.Blue,
                   var vertColor: Color = Color.Red,
                   var nodesViews: MutableMap<D, NodeView<D>> =  mutableMapOf(),
                   var vertViews: MutableList<VertView<D>> = mutableListOf()) {

    init {
        var positions = FA2Layout()
        for (i in positions) {
            nodesViews[i.key] = NodeView(positions[i.key]!!, radius, nodeColor, value = i.key)
        }
        for (i in graph.vertices) {
            for (j in i.value) {
                vertViews.add(
                    VertView(start = nodesViews[i.key]!!,
                    end = nodesViews[j.first]!!,
                    color = vertColor)
                )
            }
        }
    }
    fun applyAction(action: GraphAction) {
        TODO("Create GraphView Action Interface")
    }

    // just for fast not implemented like algoritm
    fun FA2Layout(eps: Float = 1.0E-20F, iterations: Int = 100, speed: Float = 0.1f, speedEfficiency: Float = 1f): MutableMap<D, Offset> {
        var masses: MutableMap<D, Float> = mutableMapOf()
        var positions: MutableMap<D, Offset> = mutableMapOf()
        var oldSpeeds: MutableMap<D, Offset> = mutableMapOf()
        var speeds: MutableMap<D, Offset> = mutableMapOf()
        var verts: MutableList<Pair<Pair<D, D>, Float>> = mutableListOf()

        for (i in this.graph.vertices) {
            masses[i.key] = 0f
            for (j in i.value) {
                masses[i.key] = masses[i.key]!! + j.second
                verts.add(Pair(Pair(i.key, j.first), j.second))
            }
            // Create different window size
            positions[i.key] = Offset(x = Random.nextFloat() * 1000, y = Random.nextFloat() * 800)
            oldSpeeds[i.key] = Offset(x = 0f, y = 0f)
            speeds[i.key] = Offset(x = 0f, y = 0f)
        }

        println(positions)

        for (i in 1..iterations) {
            for (node in positions) {
                oldSpeeds[node.key] = speeds[node.key]!!
            }
            for (node in positions) {
                var offset = node.value
                var distCenter = sqrt((offset.x - 500) * (offset.x - 500) + (offset.y - 400) * (offset.y - 400))

                if (distCenter > eps) {
                    var factor: Float = masses[node.key]!! * 1f / distCenter
                    speeds[node.key] = speeds[node.key]!! + Offset(x = - (offset.x - 500) * factor, y = - (offset.y - 400) * factor)
                }
            }

            for (vert in verts) {
                var n1 = vert.first.first
                var n2 = vert.first.second

                var weight = vert.second

                var dist = positions[n1]!! - positions[n2]!!
                var fact = - 1f/weight

                speeds[n1] = speeds[n1]!! + dist * fact
            }

            for (n1 in positions) {
                for (n2 in positions) {
                    var offset = positions[n1.key]!! - positions[n2.key]!!

                    var dist = offset.x * offset.x + offset.y * offset.y

                    if (dist > eps) {
                        var fact = 1f * masses[n1.key]!! * masses[n2.key]!! / dist

                        speeds[n1.key] = speeds[n1.key]!! + offset * fact
                    }
                }
            }

            for (node in positions) {
                var swinging = sqrt((oldSpeeds[node.key]!!.x - speeds[node.key]!!.x) * (oldSpeeds[node.key]!!.x - speeds[node.key]!!.x) +
                        (oldSpeeds[node.key]!!.y - speeds[node.key]!!.y) * (oldSpeeds[node.key]!!.y - speeds[node.key]!!.y))

                var fact = speed / (1 + sqrt(speed * swinging))
                positions[node.key] = positions[node.key]!! + speeds[node.key]!! * fact
                if (positions[node.key]!!.x > 990) positions[node.key] = Offset(x = 990f, y = positions[node.key]!!.y)
                if (positions[node.key]!!.x < 10) positions[node.key] = Offset(x = 10f, y = positions[node.key]!!.y)
                if (positions[node.key]!!.y > 790) positions[node.key] = Offset(x = positions[node.key]!!.x, y = 790f)
                if (positions[node.key]!!.y < 10) positions[node.key] = Offset(x = positions[node.key]!!.x, y = 10f)
            }


        }
        println(positions)
        return positions
    }
}