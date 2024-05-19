package data.db.sqlite_exposed

import data.Constants.SQLITE_APP_CONFIG
import data.db.sqlite_exposed.settings.Settings
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
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