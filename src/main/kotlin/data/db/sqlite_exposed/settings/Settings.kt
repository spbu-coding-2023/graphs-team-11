package data.db.sqlite_exposed.settings

import org.jetbrains.exposed.sql.Table

object Settings : Table() {
    val key = varchar("settingKey", 50)
    val value = varchar("settingValue", 50)

    override val primaryKey = PrimaryKey(key)
}