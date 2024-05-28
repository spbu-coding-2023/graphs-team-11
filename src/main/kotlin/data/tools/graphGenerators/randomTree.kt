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
import kotlin.random.Random

fun randomTree(n: Int, maxWeight: Int, isDirected: Boolean = true): Graph {

    val graph = if (isDirected) Graph() else UndirectedGraph()

    graph.addNode("1")
    graph.addNode("2")
    graph.addVertice("1", "2")

    if (maxWeight > 1) {
        for (i in 3..n) {
            graph.addNode("$i")
            graph.addVertice(Random.nextInt(1, i - 1).toString(), i.toString(), Random.nextInt(1, maxWeight).toFloat())
        }
    } else {
        for (i in 3..n) {
            graph.addNode(i.toString())
            graph.addVertice(Random.nextInt(1, i - 1).toString(), i.toString())
        }
    }

    return graph
}
