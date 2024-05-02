package data.algoritms

abstract class Algoritm {
    open fun alogRun() {
        println("Algo")
    }
}

class DFS(): Algoritm() {
    override fun alogRun() {
        println("DFS")
    }
}

class BFS(): Algoritm() {
    override fun alogRun() {
        println("BFS")
    }
}

class Astart(): Algoritm() {
    override fun alogRun() {
        println("A*")
    }
}