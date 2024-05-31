/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

package model.graph_model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import org.gephi.graph.api.GraphController
import org.gephi.graph.api.GraphModel
import org.gephi.graph.api.Node
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout
import org.gephi.project.api.ProjectController
import org.gephi.project.api.Workspace
import org.openide.util.Lookup
import java.util.*
import kotlin.math.max
import kotlin.math.sqrt


@Stable
data class NodeViewClass(
    var offset: Offset, var radius: Float, var color: Color, var value: String?, var shape: Shape, var alpha: Float = 1f
)

data class VertView(
    var start: NodeViewClass,
    var end: NodeViewClass,
    var color: Color,
    var alpha: Float = 1f,
    var weight: Float = 1f
)

class GraphViewClass(
    var graph: Graph,
    var radius: Float = 30f,
    var nodeColor: Color = Color.Blue,
    var vertColor: Color = Color.Blue,
    var baseShape: Shape = CircleShape,
    val isEmpty: Boolean,
    nodesViews: MutableMap<String, NodeViewClass> = mutableMapOf(),
    vertViews: MutableMap<String, MutableMap<String, VertView>> = mutableMapOf(),
    newNodes: MutableList<NodeViewClass> = mutableListOf(),
    scope: CoroutineScope,
    afterLayout: (() -> Unit)?
) {

    var nodesViews by mutableStateOf(nodesViews)
        private set
    var vertViews by mutableStateOf(vertViews)
        private set
    var newNodes by mutableStateOf(newNodes)
        private set

    var returnStack by mutableStateOf(Stack<Update>())

    var mainJob: Job

    init {
        mainJob = scope.launch {
            // if graph is empty, add mock nodes and edge to avoid algorithm returning wrong result.
            if (graph.vertices.isEmpty() && !isEmpty) {
                graph.apply {
                    addNode("1")
                }
            }
            val positions: MutableMap<String, Offset> = layout()

            for (i in positions) {
                nodesViews[i.key] = NodeViewClass(
                    offset = positions[i.key]!!,
                    radius = radius,
                    color = nodeColor,
                    value = i.key,
                    shape = baseShape
                )
            }
            for ((i, verts) in graph.vertices) {
                vertViews[i] = mutableMapOf()
                for ((j, weight) in verts) {
                    vertViews[i]!![j] = VertView(
                        start = nodesViews[i]!!,
                        end = nodesViews[j]!!,
                        color = vertColor,
                        alpha = 1f,
                        weight = weight
                    )
                }
            }

            if (afterLayout != null) {
                afterLayout()
            }
        }
    }

    fun addNode(value: String?, offset: Offset, color: Color = nodeColor) {
        if (value != null) {
            this.graph.addNode(value)
            this.nodesViews[value] = NodeViewClass(
                offset = offset, radius = radius, color = color, value = value, shape = baseShape
            )
        } else {
            newNodes.add(
                NodeViewClass(
                    offset = offset, radius = radius, color = color, value = null, shape = baseShape
                )
            )
        }

    }

    fun addVert(oneValue: String, twoValue: String, weight: Float = 1f) {
        if (oneValue in this.nodesViews && twoValue in this.nodesViews) {
            this.graph.addVertice(oneValue, twoValue, weight = weight)
            if (oneValue in this.vertViews) {
                this.vertViews[oneValue]!![twoValue] = VertView(
                    start = nodesViews[oneValue]!!, end = nodesViews[twoValue]!!, color = vertColor, alpha = 1f
                )
            } else {
                this.vertViews[oneValue] = mutableMapOf()
                this.vertViews[oneValue]!![twoValue] = VertView(
                    start = nodesViews[oneValue]!!, end = nodesViews[twoValue]!!, color = vertColor, alpha = 1f
                )
            }
        }
    }

    fun applyUpdate(update: Update, isNotReUpdate: Boolean = true) {
        val nodeViewReUpdate: MutableMap<String, NodeViewUpdate> = mutableMapOf()
        val vertViewReUpdate: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()
        for (v in update.nodeViewUpdate) {
            if (isNotReUpdate) nodeViewReUpdate[v.key] = NodeViewUpdate(
                offset = null,
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
                    color = vertViews[v]!![u]!!.color, alpha = vertViews[v]!![u]!!.alpha
                )
                if (viewUpdate.color != null) vertViews[v]!![u]!!.color = viewUpdate.color
                if (viewUpdate.alpha != null) vertViews[v]!![u]!!.alpha = viewUpdate.alpha
            }
        }
        if (isNotReUpdate) returnStack.add(Update(nodeViewUpdate = nodeViewReUpdate, vertViewUpdate = vertViewReUpdate))
    }

    fun comeBack() {
        if (this.returnStack.size > 0) this.applyUpdate(this.returnStack.pop(), isNotReUpdate = false)
    }

    // just for fast not implemented like algoritm
    suspend fun layout(maxIter: Int = 500): MutableMap<String, Offset> {
        val positions: MutableMap<String, Offset> = mutableMapOf()
        return withContext(Dispatchers.Default) {
            val pc = Lookup.getDefault().lookup(ProjectController::class.java)
            pc.newProject()
            val workspace: Workspace = pc.currentWorkspace

            val graphModel: GraphModel = Lookup.getDefault().lookup(
                GraphController::class.java
            ).getGraphModel(workspace)
            val dirGraph = graphModel.directedGraph

            val nodes: MutableMap<String, Node> = mutableMapOf()
            for ((v, _) in graph.vertices) {
                val node = graphModel.factory().newNode(v)
                nodes[v] = node
                dirGraph.addNode(node)
            }
            for ((u, neig) in graph.vertices) {
                for ((v, _) in neig) {
                    val edge = graphModel.factory().newEdge(nodes[u]!!, nodes[v]!!)
                    dirGraph.addEdge(edge)
                }
            }

            val layout = ForceAtlasLayout(null)
            layout.setGraphModel(graphModel)
            layout.resetPropertiesValues()
            layout.initAlgo()
            layout.resetPropertiesValues()

            val task = launch {
                for (i in 1..maxIter) {
                    if (layout.canAlgo()) layout.goAlgo()
                    else break
                }
            }

            task.join()

            val x = mutableListOf<Float>()
            val y = mutableListOf<Float>()
            for ((v, _) in graph.vertices) {
                x.add(nodes[v]!!.x())
                y.add(nodes[v]!!.y())
            }
            val nodePerScreen = max(x.size / 500, 1)
            if (x.size > 0) {
                val (minX, maxX) = Pair(x.min(), x.max())
                val (minY, maxY) = Pair(y.min(), y.max())
                for ((v, _) in graph.vertices) {
                    positions[v] = Offset(
                        x = nodePerScreen - 2 * nodePerScreen * (nodes[v]!!.x() - minX) / (maxX - minX),
                        y = nodePerScreen - 2 * nodePerScreen * (nodes[v]!!.y() - minY) / (maxY - minY)
                    )
                }
            }
            positions
        }
    }
}

fun abs(offset: Offset): Float {
    val t = sqrt(offset.x * offset.x + offset.y * offset.y)
    return t
}
