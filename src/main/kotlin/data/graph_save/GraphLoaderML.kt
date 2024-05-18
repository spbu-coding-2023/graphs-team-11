package data.graph_save

import model.graph_model.Graph
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File

class GraphLoaderML(path: String) : Graph<String>() {
    val graph: Graph<String> = Graph()

    init {
        var weighted = false
        var cheakNext = false
        var saveSplit: List<String> = listOf()

        File(path).forEachLine {
            if (cheakNext && "<data key=" in it) {
                val weight = it.split(">")[1].split("<")[0].toFloat()
                var sourse = ""
                var target = ""
                for (i in saveSplit.indices) {
                    if (saveSplit[i].endsWith("source=")) sourse = saveSplit[i + 1]
                    if (saveSplit[i].endsWith("target=")) target = saveSplit[i + 1]
                }
                graph.addVertice(sourse, target, weight = weight)

                cheakNext = false
            } else {
                if (cheakNext) {
                    var sourse = ""
                    var target = ""
                    for (i in saveSplit.indices) {
                        if (saveSplit[i].endsWith("source=")) sourse = saveSplit[i + 1]
                        if (saveSplit[i].endsWith("target=")) target = saveSplit[i + 1]
                    }
                    graph.addVertice(sourse, target)
                }
                when {
                    ("attr.name=\"weight\"" in it) -> {
                        weighted = true
                    }

                    ("<node " in it) -> {
                        val id = it.split("\"")[1]
                        graph.addNode(id)
                    }

                    ("<edge " in it) -> {
                        val splitedEdge = it.split("\"")
                        if (!weighted) {
                            var sourse = ""
                            var target = ""
                            for (i in splitedEdge.indices) {
                                if (splitedEdge[i].endsWith("source=")) sourse = splitedEdge[i + 1]
                                if (splitedEdge[i].endsWith("target=")) target = splitedEdge[i + 1]
                            }
                            graph.addVertice(sourse, target)
                        } else {
                            saveSplit = splitedEdge
                            cheakNext = true
                        }
                    }
                }
            }
        }
    }
}
