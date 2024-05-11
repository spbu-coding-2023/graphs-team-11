package data.db.sqlite_exposed

import data.db.sqlite_exposed.edge.Edges
import data.db.sqlite_exposed.graph.Graphs
import model.graph_model.Graph
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun connect() {
    Database.connect("jdbc:sqlite:sqlite_exposed_graph.db", driver = "org.sqlite.JDBC")
    transaction {
        SchemaUtils.create(Graphs, Edges)
    }
}

fun saveGraph(graph: Graph<Any>, name: String): Int {
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
                    it[Edges.edgeSource] = source.toString()
                    it[Edges.edgeTarget] = target.toString()
                }
            }
        }

        graphId
    }
}


fun <D> getGraph(id: Int): Pair<Graph<Any>?, String?> {
    return transaction {
        val graphEntity = data.db.sqlite_exposed.graph.Graph.findById(id) ?: return@transaction Pair(null, null)
        Pair(deserializeGraph(graphEntity.data, graphEntity.size), graphEntity.name)
    }
}

fun getAllGraphs(): List<Triple<Int, Graph<Any>, String>> {
    return transaction {
        data.db.sqlite_exposed.graph.Graph.all().map { entity ->
            Triple(entity.id.value, deserializeGraph(entity.data, entity.size), entity.name)
        }
    }
}


fun deleteGraph(id: Int): Pair<Graph<Any>?, String?> {
    return transaction {
        val graphEntity = data.db.sqlite_exposed.graph.Graph.findById(id) ?: return@transaction Pair(null, null)
        val graph = deserializeGraph(graphEntity.data, graphEntity.size)
        val name = graphEntity.name
        graphEntity.delete()
        Pair(graph, name)
    }
}

private fun serializeGraph(graph: Graph<Any>): String {
    val serializedVertices = graph.vertices.entries.joinToString(separator = "|") { (source, neighbors) ->
        "$source:${neighbors.joinToString(";") { it.toString() }}"
    }
    return "$serializedVertices:${graph.size}"
}

private fun deserializeGraph(data: String, size: Int): Graph<Any> {
    val graph = Graph<Any>()
    val entries = data.split("|")
    val vertices = mutableMapOf<Any, MutableSet<Pair<Any, Float>>>()

    for (entry in entries) {
        val parts = entry.split(":")
        val source = parts[0].toInt() as Any

        val neighbors = parts[1].split(";")
        val h1 = neighbors[0]
        val h2 = h1.split(", ")

        val hhh = neighbors.mapNotNull { it ->
            val neighborParts = it.split(", ")

            if (neighborParts.size == 2) {
                val vertex = neighborParts[0].removePrefix("(").toInt()
                val weight = neighborParts[1].removeSuffix(")").toFloat()
                Pair(vertex as Any, weight)
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


