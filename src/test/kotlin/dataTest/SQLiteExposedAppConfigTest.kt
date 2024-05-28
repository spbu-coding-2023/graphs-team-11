package dataTest

import data.db.sqlite_exposed.connectConfig
import data.db.sqlite_exposed.getTheme
import data.db.sqlite_exposed.setTheme
import org.junit.jupiter.api.BeforeAll
import ui.theme.Theme
import kotlin.test.Test
import kotlin.test.assertEquals

// need to fix this later!

/**
 * A test class to check the correct operation of the module for storing settings in the database, namely the theme.
 */
class SQLiteExposedAppConfigTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            connectConfig()
        }
    }

    @Test
    fun `test getTheme returns default value because DB is empty`() {
        val currentTheme = getTheme()
        assertEquals(Theme.LIGHT, currentTheme)
    }

    @Test
    fun `test setTheme and getTheme functions`() {
        setTheme(Theme.DARK)

        val currentTheme = getTheme()
        assertEquals(Theme.DARK, currentTheme)

        setTheme(Theme.LIGHT)

        val newCurrentTheme = getTheme()
        assertEquals(Theme.LIGHT, newCurrentTheme)
    }

}