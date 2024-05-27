package uiTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import data.Constants.CHOOSE_GRAPH_WINDOW_TITLE
import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.connectConfig
import kotlinx.coroutines.*
import org.junit.Rule
import ui.IntroView
import ui.IntroWindowView
import ui.MainWindow
import ui.components.MyApplicationState
import ui.components.MyWindowState
import ui.theme.Theme
import viewmodel.IntroWindowVM
import kotlin.math.max
import kotlin.test.BeforeTest
import kotlin.test.Test
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

    @BeforeTest
    fun setup() {
        runBlocking {
            connectDatabase()
            applicationState = MyApplicationState(scope)
            state = applicationState.windows.first()
            viewModel = IntroWindowVM(isSettingMenuOpen, scope)
        }
    }

    private suspend fun connectDatabase() {
        withContext(Dispatchers.Default) {
            connectConfig()
            connect()
        }
    }


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

    @Test
    fun `test Radio button changes graph type`() {
        composeTestRule.setContent {
            IntroView(viewModel, state, appTheme, scope)
        }

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

    @Test
    fun `test Saved Graph is loaded`() {
        composeTestRule.setContent {
            IntroView(viewModel, state, appTheme, scope)
        }

        if (viewModel.graphList.value.isNotEmpty()) {
            val savedGraph = viewModel.graphList.value.first()
            composeTestRule.onAllNodesWithTag("UseButton").onFirst().performClick()

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
            val newViewModel = state.mainVM

            // time depends on the graph size and local machine performance. Let's give min 1 second timeout.
            // If graph is too big, we will wait longer.
            val timeOut = (savedGraph.second.vertices.size * 10).toLong()
            composeTestRule.waitUntil(max(timeOut, 1_000)) {
                newViewModel.graphIsReady.value
            }

            // Assert the main window is open and graph is loaded.
            composeTestRule.onNodeWithTag("MainApp").assertExists()

            // compare graph that we chose to load, and the graph that is loaded.
            val loadedGraph = newViewModel.graphView.graph
            assertEquals(savedGraph.second.size, loadedGraph.size)
            for (i in 0 until savedGraph.second.size) {
                assertEquals(savedGraph.second.vertices[i.toString()], loadedGraph.vertices[i.toString()])
            }
        } else {
            println("No saved graphs to test")
            composeTestRule.onNodeWithTag("UseButton").assertDoesNotExist()
        }
    }
}
