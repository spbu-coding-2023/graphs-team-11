package model.graph_modelTests

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import data.tools.graphGenerators.randomTree
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import model.graph_model.Graph
import model.graph_model.GraphViewClass
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GraphViewClassTest {
    private lateinit var graph: Graph
    private lateinit var scope: CoroutineScope
    // No Layout Test, because it will have it's own integration test
    @BeforeEach
    fun setup() {
        graph = Graph()

        val nodes = arrayOf("1", "2", "3", "4")
        val edges = arrayOf(Pair("1", "2"), Pair("2", "3"))

        for (node in nodes) graph.addNode(node)

        for ((u, v) in edges) graph.addVertice(u, v)

        scope = CoroutineScope(Dispatchers.IO)
    }

    @Test
    fun `Init empty graph`() = runTest {
        val gv = GraphViewClass(Graph(), scope = scope, isEmpty = true, afterLayout = null)
        assert(gv.nodesViews.isEmpty())
        assert(gv.vertViews.all { it.value.isEmpty() })
    }

    @Test
    fun `Init graph`() = runTest {
        val gv = GraphViewClass(graph, scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()
        assert(gv.nodesViews.size == graph.vertices.size)
        for ((v, neig) in graph.vertices) {
            assert(v in gv.nodesViews)
            for ((u, _) in neig) {
                assert(gv.vertViews[v]?.getOrDefault(u, null) != null)
            }
        }
    }

    @Test
    fun `addNode not null`() = runTest {
        val gv = GraphViewClass(Graph(), scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()

        gv.addNode("1", offset = Offset(0f, 0f))

        assert("1" in graph.vertices)
        assert("1" in gv.nodesViews)

        assert(gv.nodesViews.size == gv.graph.vertices.size)
        for ((v, neig) in gv.graph.vertices) {
            assert(v in gv.nodesViews)
            for ((u, _) in neig) {
                assert(gv.vertViews[v]?.getOrDefault(u, null) != null)
            }
        }
    }

    @Test
    fun `addNode null`() = runTest {
        val gv = GraphViewClass(Graph(), scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()

        gv.addNode(null, offset = Offset(0f, 0f))

        assert(!gv.newNodes.isEmpty())
    }

    @Test
    fun `addVert test`() = runTest {
        val gv = GraphViewClass(graph, scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()

        gv.addVert("1", "3")

        assert(gv.nodesViews.size == graph.vertices.size)
        for ((v, neig) in graph.vertices) {
            assert(v in gv.nodesViews)
            for ((u, _) in neig) {
                assert(gv.vertViews[v]?.getOrDefault(u, null) != null)
            }
        }
    }

    @Test
    fun `applyUpdate nodeViews`() = runTest {
        val gv = GraphViewClass(graph, scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()
        gv.applyUpdate(
            Update(
                nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(radius = -200f))
            )
        )
        try {
            val view = gv.nodesViews["1"]!!
            assert(view.radius == -200f)
            assert(view.color == gv.nodeColor)
            assert(view.value == "1")
            assert(view.shape == gv.baseShape)
        } catch (e: NullPointerException) {
            assert(false)
        }
    }

    @Test
    fun `applyUpdate vertViews`() = runTest {
        val gv = GraphViewClass(graph, scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()

        gv.applyUpdate(
            Update(
                vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(color = Color.Black)))
            )
        )
        try {
            val view = gv.vertViews["1"]!!["2"]!!
            assert(view.color == Color.Black)
        } catch (e: NullPointerException) {
            assert(false)
        }
    }

    @Test
    fun comeBack() = runTest {
        val gv = GraphViewClass(graph, scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()

        val oldNodes = gv.nodesViews["1"]!!.radius
        val oldVerts = gv.vertViews["1"]!!["2"]!!.color

        gv.applyUpdate(
            Update(
                nodeViewUpdate = mutableMapOf("1" to NodeViewUpdate(radius = -200f)),
                vertViewUpdate = mutableMapOf("1" to mutableMapOf("2" to VertViewUpdate(color = Color.Black)))
            )
        )

        val youngNodes = gv.nodesViews["1"]!!.radius
        val youngVerts = gv.vertViews["1"]!!["2"]!!.color

        gv.comeBack()

        assertEquals(gv.nodesViews["1"]!!.radius, oldNodes)
        assertEquals(gv.vertViews["1"]!!["2"]!!.color, oldVerts)
        assertNotEquals(gv.nodesViews["1"]!!.radius, youngNodes)
        assertNotEquals(gv.vertViews["1"]!!["2"]!!.color, youngVerts)
    }

    @Test
    fun `layout return not NuN values`() = runTest{
        // Yes, test on random. Yes, it isn't unit.
        val gv = GraphViewClass(randomTree(1000, 1), scope = scope, isEmpty = false, afterLayout = null)
        gv.mainJob.join()

        for ((_, view) in gv.nodesViews) {
            assert(!view.offset.x.isNaN() && !view.offset.y.isNaN())
        }
    }
}