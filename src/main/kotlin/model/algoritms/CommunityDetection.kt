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

package model.algoritms

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import model.graph_model.Graph
import model.graph_model.graph_model_actions.NodeViewUpdate
import model.graph_model.graph_model_actions.Update
import model.graph_model.graph_model_actions.VertViewUpdate
import kotlin.random.Random


class LeidenToRun : Algoritm(null) {
    override fun algoRun(graph: Graph, selected: SnapshotStateMap<String, Int>): Update {
        val leidenAlgorithm = LeidenAlgorithm(graph)
        val communities = leidenAlgorithm.detectCommunities()

        val communityMap = mutableMapOf<Int, MutableList<String>>()
        communities.forEach { (node, community) ->
            if (communityMap.containsKey(community)) {
                communityMap[community]!!.add(node)
            } else {
                communityMap[community] = mutableListOf(node)
            }
        }

        val colors: MutableMap<Int, Color> = mutableMapOf()
        for ((community, _) in communityMap) {
            colors[community] = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        }

        val updateNode: MutableMap<String, NodeViewUpdate> = mutableMapOf()
        val updateVert: MutableMap<String, MutableMap<String, VertViewUpdate>> = mutableMapOf()

        for ((node, community) in communities) {
            updateNode[node] = NodeViewUpdate(color = colors[community])
            updateVert[node] = mutableMapOf()
            for ((neibour, _) in graph.vertices.getOrDefault(node, mutableSetOf())) {
                if (neibour in communityMap[community]!!) {
                    updateVert[node]!![neibour] = VertViewUpdate(color = colors[community], alpha = 1f)
                } else {
                    updateVert[node]!![neibour] = VertViewUpdate(color = Color.Gray, alpha = 0.1f)
                }
            }
        }

        return Update(nodeViewUpdate = updateNode, vertViewUpdate = updateVert)
    }
}

class LeidenAlgorithm(private val graph: Graph) {
    var communities = mutableMapOf<String, Int>()
    private var reversedGraph: Graph

    init {
        initializeCommunities()
        reversedGraph = graph.reverse()
    }

    private fun initializeCommunities() {
        graph.vertices.keys.forEach { node ->
            communities[node] = graph.vertices.keys.indexOf(node)
        }
    }

    fun detectCommunities(): Map<String, Int> {
        var improvement = true
        while (improvement) {
            improvement = localMovingAlgorithm()
            refinePartition()
        }
        return communities
    }

    fun localMovingAlgorithm(): Boolean {
        var improved = false
        graph.vertices.keys.shuffled().forEach { node ->
            val bestCommunity = findBestCommunity(node)
            if (bestCommunity != communities[node]) {
                communities[node] = bestCommunity
                improved = true
            }
        }
        return improved
    }

    fun findBestCommunity(node: String): Int {
        val neighborCommunities = mutableSetOf<Int>()

        // Add communities of outgoing neighbors
        graph.vertices[node]?.forEach { (neighbor, _) ->
            neighborCommunities.add(communities[neighbor]!!)
            graph.vertices[neighbor]?.forEach { (inNeighbor, _) ->
                neighborCommunities.add(communities[inNeighbor]!!)
            }
        }

        // Add communities of incoming neighbors
        if (graph.vertices[node]?.size == 0) {
            reversedGraph.vertices[node]?.forEach { (inNeighbor, _) ->
                neighborCommunities.add(communities[inNeighbor]!!)
            }
        }
        return neighborCommunities.maxByOrNull { community ->
            val communityConnections = (graph.vertices[node] ?: emptyList()).count { edge ->
                communities[edge.first] == community || communities.containsKey(edge.first) && communities[edge.first] == community
            }
            communityConnections
        } ?: communities[node]!!
    }

    private fun refinePartition() {
        graph.vertices.keys.forEach { node ->
            val bestCommunity = findBestCommunity(node)
            if (bestCommunity != communities[node]) {
                communities[node] = bestCommunity
            }
        }
    }
}

