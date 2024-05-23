package data.graph_save

import model.graph_model.Graph
import model.graph_model.UndirectedGraph
import java.io.File
import kotlin.IndexOutOfBoundsException

fun loadGraphML(path: String): Graph<String> {
    var graph: Graph<String> = Graph()

    val idToKey: MutableMap<String, String> = mutableMapOf()
    var curData: MutableMap<String, String> = mutableMapOf()

    var mainKeyId: String? = null
    var weightId: String? = null

    File(path).forEachLine { it ->
        val striped = it.trim(' ').split(" ")
        val tag = striped[0]

        try {

            when {
                tag.startsWith("<graph") -> {
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
                    if (data["edgedefault"] == "undirected") {
                        graph = UndirectedGraph()
                    }
                }

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
                        mainKeyId = data.getOrDefault("id", null)
                    }
                    if (data["for"] == "edge" && data["attr.type"] == "double") {
                        weightId = data.getOrDefault("id", null)
                    }
                }

                tag.startsWith("<node") -> {
                    curData["id"] = it.split("\"")[1]
                    if ("/" in it) {
                        try {
                            idToKey[curData["id"]!!] = curData["id"]!!
                            graph.addNode(curData["id"]!!)
                            curData = mutableMapOf()
                        } catch (e: NullPointerException) {
                            throw NullPointerException("Invalid File Format: No node id\nProblem in line \"$it\"")
                        }
                    }
                }

                tag.startsWith("</node") -> {
                    if ("key" in curData) {
                        try {
                            idToKey[curData["id"]!!] = curData["key"]!!
                            graph.addNode(curData["key"]!!)
                        } catch (e: NullPointerException) {
                            throw NullPointerException("Invalid File Format: No node id or key\nProblem in line \"$it\"")
                        }
                    } else {
                        try {
                            idToKey[curData["id"]!!] = curData["id"]!!
                            graph.addNode(curData["id"]!!)
                        } catch (e: NullPointerException) {
                            throw NullPointerException("Invalid File Format: No node id\nProblem in line \"$it\"")
                        }
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
                    try {
                        curData["source"] = data["source"]!!
                        curData["target"] = data["target"]!!
                    } catch (e: NullPointerException) {
                        throw NullPointerException("Invalid File Format: No edge source or target\nProblem in line \"$it\"")
                    }
                    if ("/" in it) {
                        try {
                            graph.addVertice(
                                idToKey[curData["source"]!!]!!,
                                idToKey[curData["target"]!!]!!
                            )
                        } catch (e: NullPointerException) {
                            throw NullPointerException("Invalid File Format: No edge source or target\nProblem in line \"$it\"")
                        }
                        curData = mutableMapOf()
                    }
                }

                tag.startsWith("</edge") -> {
                    if ("weight" in curData) {
                        try {
                            graph.addVertice(
                                idToKey[curData["source"]!!]!!,
                                idToKey[curData["target"]!!]!!,
                                weight = curData["weight"]!!.toFloat()
                            )
                        } catch (e: NullPointerException) {
                            throw NullPointerException(
                                "Invalid File Format: No edge source, target or weight\nProblem in line \"$it\""
                            )
                        }
                    } else {
                        try {
                            graph.addVertice(
                                idToKey[curData["source"]!!]!!,
                                idToKey[curData["target"]!!]!!
                            )
                        } catch (e: NullPointerException) {
                            throw NullPointerException(
                                "Invalid File Format: No edge source or target\nProblem in line \"$it\""
                            )
                        }
                    }
                    curData = mutableMapOf()
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            throw NullPointerException("Invalid File Format: No needed data\nProblem in line \"$it\"")
        }
    }
    return graph
}
