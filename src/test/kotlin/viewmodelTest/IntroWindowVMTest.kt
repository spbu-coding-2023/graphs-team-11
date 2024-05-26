package viewmodelTest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.db.sqlite_exposed.connect
import org.junit.jupiter.api.BeforeEach
import kotlinx.coroutines.test.*
import model.graph_model.Graph
import org.junit.jupiter.api.Assertions
import viewmodel.IntroWindowVM
import kotlin.test.Test

class IntroWindowVMTest {

    private lateinit var viewModel: IntroWindowVM
    private lateinit var isSettingMenuOpen: MutableState<Boolean>
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setup() {
        connect()
        isSettingMenuOpen = mutableStateOf(false)
        viewModel = IntroWindowVM(isSettingMenuOpen, testScope)
    }

    @Test
    fun `onSettingsPressed sets isSettingMenuOpen to true`() {
        isSettingMenuOpen.value = false

        viewModel.onSettingsPressed()

        Assertions.assertTrue(isSettingMenuOpen.value)
    }

    @Test
    fun `onDeleteGraphSqliteExposedPressed removes graph from graphList`() {
        val initialGraphs = listOf(Triple(1, Graph(), "Graph1"), Triple(2, Graph(), "Graph2"))
        viewModel.graphList.value = initialGraphs

        viewModel.onDeleteGraphSqliteExposedPressed(1)

        Assertions.assertEquals(1, viewModel.graphList.value.size)
        Assertions.assertEquals(2, viewModel.graphList.value[0].first)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator RandomTree`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Random Tree"

        val graph = viewModel.generateGraph(10)

        Assertions.assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator FlowerSnark`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Flower Snark"

        val graph = viewModel.generateGraph(10)

        Assertions.assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator StarDirected`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Star Directed"

        val graph = viewModel.generateGraph(10)

        Assertions.assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator StarUndirected`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Star Undirected"

        val graph = viewModel.generateGraph(10)

        Assertions.assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns empty graph if chosenGenerator is not recognized`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Not Recognized"

        val graph = viewModel.generateGraph(10)

        Assertions.assertNotNull(graph)
        Assertions.assertTrue(graph.vertices.isEmpty())
    }

    @Test
    fun `createEmptyGraph returns an empty graph`() {
        val graph = viewModel.createEmptyGraph()

        Assertions.assertNotNull(graph)
        Assertions.assertTrue(graph.vertices.isEmpty())
    }

    @Test
    fun `createGraphWithoutEdges returns a graph with correct number of nodes`() {
        viewModel.graphSize.value = "3"

        val graph = viewModel.createGraphWithoutEdges()

        Assertions.assertNotNull(graph)
        Assertions.assertEquals(3, graph.vertices.size)
    }
}
