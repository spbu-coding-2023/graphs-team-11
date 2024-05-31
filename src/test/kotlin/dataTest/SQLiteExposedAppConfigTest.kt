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