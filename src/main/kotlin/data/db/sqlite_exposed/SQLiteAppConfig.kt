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

package data.db.sqlite_exposed

import data.Constants.SQLITE_APP_CONFIG
import data.db.sqlite_exposed.settings.Settings
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ui.theme.Theme

val configDatabase by lazy {
    Database.connect("jdbc:sqlite:$SQLITE_APP_CONFIG", driver = "org.sqlite.JDBC")
}

fun connectConfig() {
    transaction(configDatabase) {
        SchemaUtils.create(Settings)
    }
}

fun getTheme(): Theme {
    return transaction(configDatabase) {
        Settings.selectAll().where { Settings.key eq "theme" }
            .map { Theme.valueOf(it[Settings.value]) }
            .singleOrNull() ?: Theme.LIGHT
    }
}

fun setTheme(theme: Theme) {
    transaction(configDatabase) {
        if (Settings.selectAll().where { Settings.key eq "theme" }.count() > 0) {
            Settings.update({ Settings.key eq "theme" }) {
                it[value] = theme.name
            }
        } else {
            Settings.insert {
                it[key] = "theme"
                it[value] = theme.name
            }
        }
    }
}