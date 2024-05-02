package data.algoritms

abstract class DemonAlgoritm: Algoritm() {
    override fun alogRun() {
        println("Demon")
    }
}

class Floyd: DemonAlgoritm() {
    override fun alogRun() {
        println("Floyd")
    }
}