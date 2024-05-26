package model.graph_model

class UndirectedGraph: Graph() {
    override fun addVertice(data1: String, data2: String, weight: Float) {
        this.vertices[data1]?.add(Pair(data2, weight))
        this.vertices[data2]?.add(Pair(data1, weight))
    }
}