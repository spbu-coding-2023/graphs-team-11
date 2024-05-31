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

package viewmodelTest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.db.sqlite_exposed.connect
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import model.graph_model.Graph
import org.junit.jupiter.api.BeforeEach
import viewmodel.IntroWindowVM
import kotlin.test.*

class IntroWindowVMTest {

    private lateinit var viewModel: IntroWindowVM
    private lateinit var isSettingMenuOpen: MutableState<Boolean>
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeTest
    fun setup() {
        connect()
        isSettingMenuOpen = mutableStateOf(false)
        viewModel = IntroWindowVM(isSettingMenuOpen, testScope)
    }

    @Test
    fun `onSettingsPressed sets isSettingMenuOpen to true`() {
        isSettingMenuOpen.value = false

        viewModel.onSettingsPressed()

        assertTrue(isSettingMenuOpen.value)
    }

    @Test
    fun `onDeleteGraphSqliteExposedPressed removes graph from graphList`() {
        val initialGraphs = listOf(Triple(1, Graph(), "Graph1"), Triple(2, Graph(), "Graph2"))
        viewModel.graphList.value = initialGraphs

        viewModel.onDeleteGraphSqliteExposedPressed(1)

        assertEquals(1, viewModel.graphList.value.size)
        assertEquals(2, viewModel.graphList.value[0].first)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator RandomTree`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Random Tree"

        val graph = viewModel.generateGraph(10)

        assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator FlowerSnark`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Flower Snark"

        val graph = viewModel.generateGraph(10)

        assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator StarDirected`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Star Directed"

        val graph = viewModel.generateGraph(10)

        assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns correct graph based on chosenGenerator StarUndirected`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Star Undirected"

        val graph = viewModel.generateGraph(10)

        assertNotNull(graph)
    }

    @Test
    fun `generateGraph returns empty graph if chosenGenerator is not recognized`() {
        viewModel.graphSize.value = "5"
        viewModel.chosenGenerator.value = "Not Recognized"

        val graph = viewModel.generateGraph(10)

        assertNotNull(graph)
        assertTrue(graph.vertices.isEmpty())
    }

    @Test
    fun `createEmptyGraph returns an empty graph`() {
        val graph = viewModel.createEmptyGraph()

        assertNotNull(graph)
        assertTrue(graph.vertices.isEmpty())
    }

    @Test
    fun `createGraphWithoutEdges returns a graph with correct number of nodes`() {
        viewModel.graphSize.value = "3"

        val graph = viewModel.createGraphWithoutEdges()

        assertNotNull(graph)
        assertEquals(3, graph.vertices.size)
    }
}
