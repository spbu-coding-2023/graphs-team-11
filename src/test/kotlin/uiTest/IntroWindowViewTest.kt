package uiTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import data.db.sqlite_exposed.connect
import data.db.sqlite_exposed.connectConfig
import kotlinx.coroutines.*
import org.junit.Rule
import ui.IntroWindowView
import ui.components.MyApplicationState
import ui.components.MyWindowState
import ui.theme.Theme
import viewmodel.IntroWindowVM
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class IntroWindowViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val scope = CoroutineScope(Dispatchers.Default)
    private lateinit var applicationState: MyApplicationState
    private lateinit var state: MyWindowState

    @BeforeTest
    fun setup() {
        runBlocking {
            connectDatabase()
            applicationState = MyApplicationState(scope)
            state = applicationState.windows.first()
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
        val isSettingMenuOpen = mutableStateOf(false)
        val appTheme = mutableStateOf(Theme.LIGHT)
        val viewModel = IntroWindowVM(isSettingMenuOpen, scope)

        composeTestRule.setContent {
            IntroWindowView(state, isSettingMenuOpen, appTheme, scope)
        }
        viewModel.onSettingsPressed()
        // Assert the settings menu is now open
        assertTrue(isSettingMenuOpen.value)
        composeTestRule.onNodeWithTag("SettingsWindowTag").assertExists()
    }
}
