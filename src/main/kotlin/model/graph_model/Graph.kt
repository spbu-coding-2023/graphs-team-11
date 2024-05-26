package model.graph_model

import androidx.compose.runtime.Stable
import data.graph_save.graphLoadUnified

@Stable
open class Graph{
    var vertices: MutableMap<String, MutableSet<Pair<String, Float>>> = mutableMapOf()
    var size: Int = 0

    fun addNode(data: String) {
        if (data !in this.vertices) {
            this.vertices[data] = mutableSetOf()
            size++
        }
    }

    open fun addVertice(data1: String, data2: String, weight: Float = 1f) {
        if (data1 in vertices && data2 in vertices) {
            this.vertices[data1]?.add(Pair(data2, weight))
        }
    }

    fun deleteVertice(data1: String, data2: String) {

        for (i in this.vertices[data1]!!) {
            if (i.first == data2) {
                this.vertices[data1]!!.remove(i)
                break
            }
        }
    }

    fun deleteNode(data1: String) {
        this.size -= 1
        this.vertices.remove(data1)
        for (i in vertices) {
            for (j in i.value) {
                if (j.first == data1) {
                    i.value.remove(j)
                }
            }
        }
    }

    fun reverse(): Graph {
        val reversedGraph = Graph()

        // Add all nodes to the reversed graph
        for ((node, _) in vertices) {
            reversedGraph.addNode(node)
        }

        // Add reversed edges to the reversed graph
        for ((source, neighbors) in vertices) {
            for ((target, weight) in neighbors) {
                reversedGraph.addVertice(target, source, weight)
            }
        }

        return reversedGraph
    }
}