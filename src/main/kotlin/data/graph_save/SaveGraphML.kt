package data.graph_save

import model.graph_model.Graph
import model.graph_model.UndirectedGraph
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists

fun <D> saveGraphML(path: String, graph: Graph<D>) {
    val pathFile =
        if (!File(path).toPath().exists()) {
            Path(path).createFile().toFile()
        } else {
            File(path)
        }

    var toWrite =
        """<?xml version="1.0" encoding="UTF-8"?>
<!-- This file was written by the JAVA GraphML Library.-->
<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
  <key id="weight" for="edge" attr.name="weight" attr.type="double"/>
        """

    when (graph) {
        is UndirectedGraph<D> -> toWrite += "<graph edgedefault=\"undirected\">\n"
        else -> toWrite += "<graph edgedefault=\"directed\">\n"
    }

    for ((v, _) in graph.vertices) {
        val id = v.toString()
        toWrite += "<node id=\"$id\"/>\n"
    }
    var edgeCounter = 0
    for ((v, neig) in graph.vertices) {
        for ((u, weigth) in neig) {
            val source = v.toString()
            val target = u.toString()
            if (weigth != 1f) {
                val weightString = weigth.toString()
                toWrite += """
                    <edge id="e$edgeCounter" source="$source" target="$target">
                        <data key="weight">$weightString</data>
                    </edge>\n
                """.trimIndent()
            }
            else {
                toWrite += "<edge id=\"e$edgeCounter\" source=\"$source\" target=\"$target\"/>\n"
            }
            edgeCounter++
        }
    }
    println(edgeCounter)
    toWrite += """</graph>
</graphml>"""
    pathFile.writeText(toWrite)
}