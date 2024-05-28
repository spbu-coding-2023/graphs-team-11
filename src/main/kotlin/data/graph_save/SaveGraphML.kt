/*
 *
 *  * This file is part of BDSM Graphs.
 *  *
 *  * BDSM Graphs is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * BDSM Graphs is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with . If not, see <https://www.gnu.org/licenses/>.
 *
 */

package data.graph_save


import model.graph_model.Graph
import model.graph_model.UndirectedGraph
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists

fun saveGraphML(path: String, graph: Graph) {
    val pathFile = if (!File(path).toPath().exists()) {
        Path(path).createFile().toFile()
    } else {
        File(path)
    }
    pathFile.writeText(graphToStringML(graph))
}

fun graphToStringML(graph: Graph): String {
    var toWrite = """<?xml version="1.0" encoding="UTF-8"?>
<!-- This file was written by the JAVA GraphML Library.-->
<graphml xmlns="http://graphml.graphdrawing.org/xmlns"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
  <key id="weight" for="edge" attr.name="weight" attr.type="double"/>
        """

    toWrite += when (graph) {
        is UndirectedGraph -> "<graph edgedefault=\"undirected\">\n"
        else -> "<graph edgedefault=\"directed\">\n"
    }

    for ((v, _) in graph.vertices) {
        val id = v
        toWrite += "<node id=\"$id\"/>\n"
    }
    var edgeCounter = 0
    for ((v, neig) in graph.vertices) {
        for ((u, weigth) in neig) {
            val target = u
            if (weigth != 1f) {
                val weightString = weigth.toString()
                toWrite += """<edge id="e$edgeCounter" source="$v" target="$target">""" + "\n" +
                        """<data key="weight">$weightString</data>""" + "\n" +
                "</edge>" + "\n"
            } else {
                toWrite += "<edge id=\"e$edgeCounter\" source=\"$v\" target=\"$target\"/>\n"
            }
            edgeCounter++
        }
    }
    toWrite += """</graph>
</graphml>"""
    return toWrite
}