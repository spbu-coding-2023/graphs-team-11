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

fun flowerSnark(n: Int): Graph {
    val graph = Graph()
    for (i in 0 until n) {
        graph.addNode((i * 4).toString())
        for (j in 1..3) {
            graph.addNode((i * 4 + j).toString())
            graph.addVertice((i * 4 + j).toString(), (i * 4).toString())
        }
    }

    for (i in 0 until n - 1) {
        graph.addVertice((i * 4 + 1).toString(), (i * 4 + 5).toString())
    }
    graph.addVertice((4 * n - 3).toString(), 1.toString())

    for (i in 0 until n - 1) {
        graph.addVertice((i * 4 + 2).toString(), (i * 4 + 6).toString())
    }
    graph.addVertice((4 * n - 2).toString(), 2.toString())

    for (i in 0 until n - 1) {
        graph.addVertice((i * 4 + 3).toString(), (i * 4 + 7).toString())
    }
    graph.addVertice((4 * n - 1).toString(), 3.toString())

    return graph
}