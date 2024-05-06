package algorithm.main

import androidx.compose.ui.graphics.Color
import data.Graph
import kotlinx.coroutines.processNextEventInCurrentThread
import ui.graph_view.graph_view_actions.NodeViewUpdate
import ui.graph_view.graph_view_actions.Update
import kotlin.random.Random

fun <D> detectCommunities(graph: Graph<D>) {
    val leiden = LeidenAlgorithm(graph)
    val communities = leiden.detectCommunities()

    // print community index stating with 1 and nodes that belong to that community
    var communityIndex = 1
    val communityMap = mutableMapOf<Int, MutableList<D>>()
    communities.forEach { (node, community) ->
        if (communityMap.containsKey(community)) {
            communityMap[community]!!.add(node)
        } else {
            communityMap[community] = mutableListOf(node)
        }
    }
    communityMap.forEach { (_, nodes) ->
        println("Community $communityIndex: $nodes")
        ++communityIndex
    }

}

class LeidenToRun: Algoritm() {
    override fun <D> alogRun(graph: Graph<D>): Update<D> {
        val leidenAlgorithm = LeidenAlgorithm<D>(graph)
        val communities = leidenAlgorithm.detectCommunities()

        val communityMap = mutableMapOf<Int, MutableList<D>>()
        var communityIndex = 0
        communities.forEach { (node, community) ->
            if (communityMap.containsKey(community)) {
                communityMap[community]!!.add(node)
            } else {
                communityMap[community] = mutableListOf(node)
            }
        }
        communityMap.forEach { (_, nodes) ->
            println("Community $communityIndex: $nodes")
            ++communityIndex
        }

        var colors: MutableMap<Int, Color> = mutableMapOf()
        for ((community, _) in communityMap) { colors[community] = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))}


        val update: MutableMap<D, NodeViewUpdate<D>> = mutableMapOf()
        for ((node, community) in communities) {
            update[node] = NodeViewUpdate(color = colors[community])
        }

        return Update<D>(nodeViewUpdate = update)
    }
}

class LeidenAlgorithm<D>(private val graph: Graph<D>) {
    private var communities = mutableMapOf<D, Int>()
    private var reversedGraph: Graph<D>

    init {
        initializeCommunities()
        reversedGraph = graph.reverse()
    }

    private fun initializeCommunities() {
        graph.vertices.keys.forEach { node ->
            communities[node] = graph.vertices.keys.indexOf(node)
        }
    }

    fun detectCommunities(): Map<D, Int> {
        var improvement = true
        while (improvement) {
            improvement = localMovingAlgorithm()
            refinePartition()
        }
        return communities
    }

    private fun localMovingAlgorithm(): Boolean {
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

    private fun findBestCommunity(node: D): Int {
        val neighborCommunities = mutableSetOf<Int>()

        // Add communities of outgoing neighbors
        graph.vertices[node]?.forEach { (neighbor, _) ->
            neighborCommunities.add(communities[neighbor]!!)
            graph.vertices[neighbor]?.forEach { (inNeighbor, _) ->
                neighborCommunities.add(communities[inNeighbor]!!)
            }
        }


        // Add communities of incoming neighbors
        reversedGraph.vertices[node]?.forEach { (inNeighbor, _) ->
            neighborCommunities.add(communities[inNeighbor]!!)
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

