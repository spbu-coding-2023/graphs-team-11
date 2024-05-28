/*
 * This file is part of BDSM Graphs.
 * 
 * BDSM Graphs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BDSM Graphs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with . If not, see <https://www.gnu.org/licenses/>.
 */

package uiTest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import data.Constants.CHOOSE_GRAPH_WINDOW_TITLE
import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.connectConfig
import kotlinx.coroutines.*
import model.graph_model.Graph
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ui.IntroView
import ui.IntroWindowView
import ui.MainWindow
import ui.components.MyApplicationState
import ui.components.MyWindowState
import ui.theme.Theme
import viewmodel.IntroWindowVM
import viewmodel.MainVM
import kotlin.math.max
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class IntroWindowViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val scope = CoroutineScope(Dispatchers.Default)
    private lateinit var applicationState: MyApplicationState
    private lateinit var state: MyWindowState
    private lateinit var viewModel: IntroWindowVM
    private val isSettingMenuOpen = mutableStateOf(false)
    private val appTheme = mutableStateOf(Theme.LIGHT)

    @Before
    fun setup() {
        System.setProperty("java.awt.headless", "false")

        println("Starting setup...")

        runBlocking {
            connectDatabase()
            applicationState = MyApplicationState(scope)
            state = applicationState.windows.first()
            viewModel = IntroWindowVM(isSettingMenuOpen, scope)

            println("Setup complete. Initializing content...")

            composeTestRule.setContent {
                IntroView(viewModel, state, appTheme, scope)
            }

            println("Setup and initialization complete.")
        }
    }

    private suspend fun connectDatabase() {
        withContext(Dispatchers.Default) {
            connectConfig()
            connect()
        }
    }

    private fun testReloadGraph(newViewModel: MutableState<MainVM?>, graphSize: Int): Graph {
        // window will reload, and state will change when it is reloaded. Let's wait for it.
        val currState = state
        composeTestRule.waitUntil(2000) {
            applicationState.windows.first() != currState
        }

        // state is updated, let's get the new state
        state = applicationState.windows.first()

        // make sure previous window is closed
        composeTestRule.onNodeWithText(CHOOSE_GRAPH_WINDOW_TITLE).assertDoesNotExist()

        composeTestRule.setContent {
            MainWindow(state, isSettingMenuOpen, appTheme)
        }

        newViewModel.value = state.mainVM

        // time depends on the graph size and local machine performance. Let's give min 1 second timeout.
        // If graph is too big, we will wait longer.
        val timeOut = (graphSize * 10).toLong()
        composeTestRule.waitUntil(max(timeOut, 1_000)) {
            newViewModel.value!!.graphIsReady.value
        }

        // Assert the main window is open and graph is loaded.
        composeTestRule.onNodeWithTag("MainApp").assertExists()

        // compare graph that we chose to load, and the graph that is loaded.
        return newViewModel.value!!.graphView.graph
    }


    /**
     * Test settings menu open.
     */
    @Test
    fun testSettingsMenuOpens() {
        val viewModel = IntroWindowVM(isSettingMenuOpen, scope)

        composeTestRule.setContent {
            IntroWindowView(state, isSettingMenuOpen, appTheme, scope)
        }
        viewModel.onSettingsPressed()
        // Assert the settings menu is now open
        assertTrue(isSettingMenuOpen.value)
        composeTestRule.onNodeWithTag("SettingsWindowTag").assertExists()
    }

    /**
     * Test radio button changes graph type.
     *
     * Check that when you change the method of loading the graph (Saved, Manual, Generate, Empty),
     * the window actually changes to the desired one.
     */
    @Test
    fun `test Radio button changes graph type`() {


        for (graphType in viewModel.graphTypes) {
            composeTestRule.onNodeWithText(graphType).performClick()
            assertEquals(viewModel.chosenGraph.value, graphType)

            when (graphType) {
                "Saved" -> {
                    composeTestRule.onNodeWithTag("SavedGraphsList").assertExists()

                    composeTestRule.onNodeWithTag("ButtonForEmpty").assertDoesNotExist()
                    composeTestRule.onNodeWithTag("CreateGraphButton").assertDoesNotExist()
                }

                "Manual" -> {
                    composeTestRule.onNodeWithTag("IntTextField").assertExists()
                    composeTestRule.onNodeWithTag("CreateGraphButton").assertExists()
                }

                "Generate" -> {
                    composeTestRule.onNodeWithTag("GenerateGraphSettings").assertExists()
                    composeTestRule.onNodeWithTag("CreateGraphButton").assertExists()
                }

                "Empty" -> {
                    composeTestRule.onNodeWithTag("SavedGraphsList").assertDoesNotExist()
                    composeTestRule.onNodeWithTag("IntTextField").assertDoesNotExist()
                    composeTestRule.onNodeWithTag("GenerateGraphSettings").assertDoesNotExist()
                    composeTestRule.onNodeWithTag("ButtonForEmpty").assertExists()
                }
            }
        }
    }

    /**
     * Integration test of the module of graph loading from SQLite databas and the graph view module
     *
     * Step 1: Take a graph. Save it and upload it to the database.
     * At the same time we check that the window works correctly - it closes and opens correctly
     *
     * Step 2. Check the equality of the old and new graphs
     *
     */
    @Test
    fun `test Saved Graph is loaded`() {
        if (viewModel.graphList.value.isNotEmpty()) {
            val savedGraph = viewModel.graphList.value.first().second
            composeTestRule.onAllNodesWithTag("UseButton").onFirst().performClick()

            val newViewModel: MutableState<MainVM?> = mutableStateOf(null)

            val loadedGraph = testReloadGraph(newViewModel, savedGraph.vertices.size)

            assertEquals(savedGraph.size, loadedGraph.size)
            for (i in 0 until savedGraph.size) {
                assertEquals(savedGraph.vertices[i.toString()], loadedGraph.vertices[i.toString()])
            }
        } else {
            println("No saved graphs to test")
            composeTestRule.onNodeWithTag("UseButton").assertDoesNotExist()
        }
    }

    /**
     * Test the integration of the GraphViewModelClass class into the main application,
     * by generating an empty graph and checking that it is displayed.
     *
     * Step 1: Generate an empty graph and check
     * that the window will reload when the graph is replaced in the application
     *
     * Step 2: Check that the size of the graph displayed in the window corresponds to the one we have loaded
     */
    @Test
    fun `test generate graph with no edges (Manual)`() {
        viewModel.chosenGraph.value = "Manual"
        viewModel.graphSize.value = "100"
        val graphSize = viewModel.graphSize.value.toInt()
        composeTestRule.onNodeWithTag("CreateGraphButton").performClick()

        val newViewModel: MutableState<MainVM?> = mutableStateOf(null)

        val loadedGraph = testReloadGraph(newViewModel, graphSize)

        assertEquals(graphSize, loadedGraph.size)
        for (i in 0 until graphSize) {
            assertEquals(0, loadedGraph.vertices[i.toString()]!!.size)
        }
    }

    /**
     * Test generate random graph with random edge weight
     *
     * Checking for correctness of a weighted random tree created via the user interface.
     * And also that it is actually displayed.
     */
    @Test
    fun `test generate random graph with random edge weight`() {
        viewModel.chosenGraph.value = "Generate"
        viewModel.graphSize.value = "100"
        val graphSize = viewModel.graphSize.value.toInt()
        viewModel.chosenGenerator.value = "Random Tree"
        viewModel.weightMax.value = "10"
        composeTestRule.onNodeWithTag("CreateGraphButton").performClick()

        val newViewModel: MutableState<MainVM?> = mutableStateOf(null)

        val loadedGraph = testReloadGraph(newViewModel, graphSize)

        assertEquals(graphSize, loadedGraph.size)
        for (i in 1..graphSize) {
            loadedGraph.vertices[i.toString()]!!.forEach { (_, weight) ->
                assertTrue { weight <= viewModel.weightMax.value.toInt().toFloat() && weight >= 1f }
            }
        }
    }

    /**
     * Test generate random graph with random edge weight
     *
     * Checking for correctness of unweighted random tree created via the user interface.
     * And also that it is actually displayed.
     *
     */
    @Test
    fun `test generate random graph with edge weight 1`() {
        viewModel.chosenGraph.value = "Generate"
        viewModel.graphSize.value = "100"
        val graphSize = viewModel.graphSize.value.toInt()
        viewModel.chosenGenerator.value = "Random Tree"
        viewModel.weightMax.value = "1"
        composeTestRule.onNodeWithTag("CreateGraphButton").performClick()

        val newViewModel: MutableState<MainVM?> = mutableStateOf(null)

        val loadedGraph = testReloadGraph(newViewModel, graphSize)

        assertEquals(graphSize, loadedGraph.size)
        for (i in 1..graphSize) {
            loadedGraph.vertices[i.toString()]!!.forEach { (_, weight) ->
                assertEquals(1f, weight)
            }
        }
    }

    /**
     * Test generate random graph with random edge weight
     *
     * Checking for correctness of flower snark created via the user interface.
     * And also that it is actually displayed.
     *
     */
    @Test
    fun `test generate flower snark graph`() {
        viewModel.chosenGraph.value = "Generate"
        viewModel.graphSize.value = "10"
        val graphSize = viewModel.graphSize.value.toInt()
        viewModel.chosenGenerator.value = "Flower Snark"
        composeTestRule.onNodeWithTag("CreateGraphButton").performClick()

        val newViewModel: MutableState<MainVM?> = mutableStateOf(null)

        val loadedGraph = testReloadGraph(newViewModel, graphSize)

        // multiply by 4 because flower snark graph has 4 times more vertices than the given size
        assertEquals(graphSize * 4, loadedGraph.size)
    }

}
