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

package data.tools.graphGenerators

import model.graph_model.Graph
import model.graph_model.UndirectedGraph

fun starDirected(n: Int): Graph {
    val graph = Graph()
    graph.addNode("1")
    for (i in 2..n) {
        val curr = i.toString()
        graph.addNode(curr)
        graph.addVertice("1", curr)
    }
    return graph
}

fun starUndirected(n: Int): Graph {
    val graph = UndirectedGraph()
    graph.addNode("1")
    for (i in 2..n) {
        val curr = i.toString()
        graph.addNode(curr)
        graph.addVertice("1", curr)
    }
    return graph
}
