package data.graph_save

import model.graph_model.Graph
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.util.Stack

class GraphLoaderML(path: String) : Graph<String>() {
    val graph: Graph<String> = Graph()

    init {
        val idToKey: MutableMap<String, String> = mutableMapOf()
        var curData: MutableMap<String, String> = mutableMapOf()

        var mainKeyId: String? = null
        var weightId: String? = null

        File(path).forEachLine {
            val striped = it.strip().split(" ")
            val tag = striped[0]

            when {
                tag.startsWith("<key") -> {
                    val data = mutableMapOf<String, String>()
                    it.split(" ").forEach {
                        if ("=" in it) {
                            data[it.split("=")[0]] =
                                it.split("=")[1]
                                    .trim('>')
                                    .trim('/')
                                    .trim('"')
                        }
                    }
                    if (data["attr.type"] == "string" && data["for"] == "node" && mainKeyId == null) {
                        mainKeyId = data["id"]!!
                    }
                    if (data["for"] == "edge" && data["attr.type"] == "double") {
                        println("HUI")
                        weightId = data["id"]!!
                        println(weightId)
                    }
                }
                tag.startsWith("<node") -> {
                    curData["id"] = it.split("\"")[1]

                    if ("/" in it) {
                        idToKey[curData["id"]!!] = curData["id"]!!
                        graph.addNode(curData["id"]!!)
                        curData = mutableMapOf()
                    }
                }
                tag.startsWith("</node") -> {
                    if ("key" in curData) {
                        idToKey[curData["id"]!!] = curData["key"]!!
                        graph.addNode(curData["key"]!!)
                    } else {
                        idToKey[curData["id"]!!] = curData["id"]!!
                        graph.addNode(curData["id"]!!)
                    }
                    curData = mutableMapOf()
                }
                tag.startsWith("<data") -> {
                    val id = it.split("\"")[1]
                    if (mainKeyId == null) {
                        curData["key"] = it.split(">")[1].split("<")[0]
                    } else {
                        if (id == mainKeyId) {
                            curData["key"] = it.split(">")[1].split("<")[0]
                        }
                    }
                    if (weightId != null) {
                        if (id == weightId) {
                            curData["weight"] = it.split(">")[1].split("<")[0]
                        }
                    }

                }
                tag.startsWith("<edge") -> {
                    val data = mutableMapOf<String, String>()
                    it.split(" ").forEach {
                        if ("=" in it) {
                            data[it.split("=")[0]] =
                                it.split("<")[0]
                                    .split("=")[1]
                                    .trim('>')
                                    .trim('/')
                                    .trim('"')

                        }
                    }
                    println(data)
                    println(idToKey)
                    curData["source"] = data["source"]!!
                    curData["target"] = data["target"]!!
                    if ("/" in it) {
                        graph.addVertice(
                            idToKey[curData["source"]!!]!!,
                            idToKey[curData["target"]!!]!!
                        )
                        curData = mutableMapOf()
                    }
                }
                tag.startsWith("</edge") -> {
                    println(curData)
                    if ("weight" in curData) {
                        graph.addVertice(
                            idToKey[curData["source"]!!]!!,
                            idToKey[curData["target"]!!]!!,
                            weight = curData["weight"]!!.toFloat())
                    } else {
                        graph.addVertice(
                            idToKey[curData["source"]!!]!!,
                            idToKey[curData["target"]!!]!!
                        )
                    }
                    curData = mutableMapOf()
                }
            }
        }
    }
}
