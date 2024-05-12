package data.db.sqlite_exposed.edge

import data.db.sqlite_exposed.graph.Graphs
import org.jetbrains.exposed.dao.id.IntIdTable

object Edges : IntIdTable() {
    val graphId = reference("graph_id", Graphs.id)
    val edgeSource = varchar("source", length = 50)
    val edgeTarget = varchar("target", length = 50)
}
