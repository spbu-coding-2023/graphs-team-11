package data.db.sqlite_exposed

import data.Constants.SQLITE_DB
import data.db.sqlite_exposed.edge.Edges
import data.db.sqlite_exposed.graph.Graphs
import model.graph_model.Graph
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

val graphDatabase by lazy {
    Database.connect("jdbc:sqlite:$SQLITE_DB", driver = "org.sqlite.JDBC")
}

fun connect() {
    transaction(graphDatabase) {
        SchemaUtils.create(Graphs, Edges)
    }
}

fun saveGraph(graph: Graph, name: String) {
    return transaction {
        val graphId = Graphs.insertAndGetId {
            it[Graphs.name] = name
            it[data] = serializeGraph(graph)
            it[size] = graph.size
        }.value

        graph.vertices.forEach { (source, neighbors) ->
            neighbors.forEach { target ->
                Edges.insert {
                    it[Edges.graphId] = EntityID(graphId, Graphs)
                    it[edgeSource] = source
                    it[edgeTarget] = target.toString()
                }
            }
        }
    }
}

fun getAllGraphs(): List<Triple<Int, Graph, String>> {
    return transaction {
        data.db.sqlite_exposed.graph.Graph.all().map { entity ->
            Triple(entity.id.value, deserializeGraph(entity.data, entity.size), entity.name)
        }
    }
}


fun deleteGraph(id: Int) {
    transaction {
        val graphEntity = data.db.sqlite_exposed.graph.Graph.findById(id) ?: return@transaction Pair(null, null)
        graphEntity.delete()
    }
}

fun serializeGraph(graph: Graph): String {
    val serializedVertices = graph.vertices.entries.joinToString(separator = "|") { (source, neighbors) ->
        "$source:${neighbors.joinToString(";") { it.toString() }}"
    }
    return "$serializedVertices:${graph.size}"
}

private fun deserializeGraph(data: String, size: Int): Graph {
    val graph = Graph()
    val entries = data.split("|")
    val vertices = mutableMapOf<String, MutableSet<Pair<String, Float>>>()

    for (entry in entries) {
        val parts = entry.split(":")
        val source = parts[0]

        val neighbors = parts[1].split(";")
        val hhh = neighbors.mapNotNull {
            val neighborParts = it.split(", ")

            if (neighborParts.size == 2) {
                val vertex = neighborParts[0].removePrefix("(")
                val weight = neighborParts[1].removeSuffix(")").toFloat()
                Pair(vertex, weight)
            } else {
                null
            }
        }.toMutableSet()
        vertices[source] = hhh
    }

    graph.vertices = vertices
    graph.size = size
    return graph
}

