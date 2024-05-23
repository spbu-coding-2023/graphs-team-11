package model.graph_model

class UndirectedGraph<D>: Graph<D>() {
    override fun addVertice(data1: D, data2: D, weight: Float) {
        this.vertices[data1]?.add(Pair(data2, weight))
        this.vertices[data2]?.add(Pair(data1, weight))
    }
}