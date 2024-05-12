package data.db.sqlite_exposed.edge

import data.db.sqlite_exposed.graph.Graph
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Edge(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Edge>(Edges)

    var graph by Graph referencedOn Edges.graphId
    var source by Edges.edgeSource
    var target by Edges.edgeTarget
}

