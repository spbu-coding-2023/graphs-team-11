package dataTest

import data.db.sqlite_exposed.connectConfig
import data.db.sqlite_exposed.getTheme
import data.db.sqlite_exposed.setTheme
import data.db.sqlite_exposed.settings.Settings
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import ui.theme.Theme
import kotlin.test.Test
import kotlin.test.assertEquals

class SQLiteExposedAppConfigTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            connectConfig()
        }
    }

    @Test
    fun `test getTheme returns default value`() {
        val currentTheme = getTheme()
        assertEquals(Theme.LIGHT, currentTheme)
    }

    @Test
    fun `test theme DB doesn't contain theme`() {
        var dbCount = -1
        // Clear the DB because we need to make sure that the theme is not set
        transaction{
            Settings.deleteAll()
            dbCount = Settings.selectAll().where { Settings.key eq "theme" }.count().toInt()
        }
        assertEquals(dbCount, 0)

        setTheme(Theme.DARK)

        transaction {
            dbCount = Settings.selectAll().where { Settings.key eq "theme" }.count().toInt()
        }
        assertEquals(dbCount, 1)
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