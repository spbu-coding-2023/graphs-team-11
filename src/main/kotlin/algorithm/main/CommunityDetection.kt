package algorithm.main

import data.Graph

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

