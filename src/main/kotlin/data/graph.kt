package data

import androidx.compose.runtime.Stable

@Stable
open class Graph<D> {
    public var vertices: MutableMap<D, MutableSet<Pair<D, Float>>> = mutableMapOf()
    public var size: Int = 0;

    fun addNode(data: D){
        this.vertices[data] = mutableSetOf()
        size++
    }

    fun addVertice(data1: D, data2: D, weight: Float = 1f){
            this.vertices[data1]?.add(Pair(data2, weight))
    }

    fun deleteVertice(data1: D, data2: D){
        for (i in this.vertices[data1]!!) {
            if (i.first == data2) {
                this.vertices[data1]!!.remove(i)
                break
            }
        }
    }

    fun deleteNode(data1: D) {
        this.vertices.remove(data1)
        for (i in vertices) {
            for (j in i.value) {
                if (j.first == data1) {
                    i.value.remove(j)
                }
            }
        }
    }

    fun reverse(): Graph<D> {
        val reversedGraph = Graph<D>()

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