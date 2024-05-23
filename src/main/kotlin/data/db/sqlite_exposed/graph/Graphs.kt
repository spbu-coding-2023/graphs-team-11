package data.db.sqlite_exposed.graph

import org.jetbrains.exposed.dao.id.IntIdTable

object Graphs : IntIdTable() {
    val name = varchar("name", 255)
    val data = text("data")
    val size = integer("size")
}